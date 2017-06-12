var stompClient = null;

var rolesTemplate = $('#rolesTemplate').html();
Mustache.parse(rolesTemplate);

var playerTemplate = $('#voteTemplate').html();
Mustache.parse(rolesTemplate);


function RoleConfig(roleName) {
    this.roleName = roleName;
    this.minimumPlayers = 0;
    this.randomChancePlayers = 0;
    var Current = this;

    var tableRow = $('<tr></tr>');
    $('#roleTableBody').append(tableRow);

    tableRow.on('change', function (e) {
        var property = $(e.target).attr('data-property');
        Current[property] = $(e.target).val();
    });

    this.render = function () {
        tableRow.html(Mustache.render(rolesTemplate, this));
    };
}

function PlayerVote(playerName) {
    this.name = playerName;
    this.votes = 0;
    var playersVotedForUser = {};
    var Current = this;

    var tableRow = $('<tr></tr>');
    $('#voteTableBody').append(tableRow);

    this.render = function () {
        tableRow.html(Mustache.render(playerTemplate, Current));
    };

    this.adminUserAssigned = function (roles) {
        $(roles).each(function (i, role) {
            if (role.player === Current.name) {
                Current.role = role.role;
            }
        });
        this.render();
    };

    this.playerVoted = function (source, target) {
        if (target === this.name) {
            playersVotedForUser[source] = true;
        } else {
            delete playersVotedForUser[source];
        }
        Current.votes = Object.keys(playersVotedForUser).length;
        this.render();
    };

    this.playerLeft = function () {
        tableRow.remove()
    }
}

$(function () {

    var roleConfigList = [];
    var playerList = [];

    $.get('role').done(function (response) {

        $(response).each(function (i, role) {
            var roleConfig = new RoleConfig(role.name);
            roleConfigList.push(roleConfig);
            roleConfig.render();
        })
    });

    function connect() {
        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function () {

            stompClient.subscribe('/topic/greetings', function (greeting) {
                showGreeting(JSON.parse(greeting.body).content);
            });

            stompClient.subscribe('/user/queue/game/join', function (newPlayersEvent) {
                $(JSON.parse(newPlayersEvent.body)).each(function (i, player) {
                    newPlayer(player)
                })
            });

            stompClient.subscribe('/user/queue/player/left', function (playerLeftEvetn) {
                $.grep(playerList, function (player) {
                    if (player.name === playerLeftEvetn.body) {
                        player.playerLeft();
                        toastr.error(player.name + ' left the game', 'Player Left');
                        return true;
                    }
                    return false;
                })
            });

            stompClient.subscribe('/user/queue/notification', function (notificationEvent) {
                var notification = JSON.parse(notificationEvent.body);
                toastr[notification.type](notification.body, notification.title)
            });
        });

        $('#assignRolesBtn').click(function (e) {
            stompClient.send('/app/admin/role/assign', {}, JSON.stringify(roleConfigList));
        });

        $('#joinGameBtn').on('click', function (e) {
            gameRequest('/app/game/join');

            stompClient.subscribe('/user/queue/role/assigned', function (roleEvent) {
                var role = JSON.parse(roleEvent.body);
                console.log(role);
                toastr.success('You are a ' + role.name, 'Role Assigned');
                $('#roleNameStr').text(role.name);
                $('#roleDescriptionStr').text(role.description);
                $('#teamStr').text(role.team.name);
                $('#roleRow').show();
            });

        });

        $('#voteTableBody').on('click', function (e) {
            var votePlayer = $(e.target).attr('data-name');
            stompClient.send('/app/player/vote', {}, votePlayer);
        });

        $('#createGameBtn').on('click', function (e) {
            gameRequest('/app/game/create');
            $('#adminRow').show();

            stompClient.subscribe('/user/queue/admin/role/assign', function (roleEvent) {
                var roles = JSON.parse(roleEvent.body);
                $(playerList).each(function (i, player) {
                    player.adminUserAssigned(roles);
                })
            });
        });

        function newPlayer(name) {
            toastr.success(name + ' joined the game', 'Player Joined');
            var playerVote = new PlayerVote(name);
            playerList.push(playerVote);
            playerVote.render();
        }

        function gameRequest(url) {
            var gameId = $('#GameIdInput').val();
            var name = $('#NameInput').val();
            var request = JSON.stringify({name: name, id: gameId});
            $('#joinGameRow').hide();
            $('#votesRow').show();
            stompClient.send(url, {}, request);

            stompClient.subscribe('/user/queue/player/joined', function (newPlayerEvent) {
                newPlayer(newPlayerEvent.body)
            });

            stompClient.subscribe('/user/queue/player/voted', function (voteEvent) {
                var vote = JSON.parse(voteEvent.body);
                toastr.warning(vote.sourcePlayer + ' voted for ' + vote.targetPlayer, 'Player Voted')
                $(playerList).each(function (i, player) {
                    player.playerVoted(vote.sourcePlayer, vote.targetPlayer)
                })
            })
        }

    }

    connect();

});


