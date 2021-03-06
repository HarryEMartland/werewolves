var stompClient = null;

var rolesTemplate = $('#rolesTemplate').html();
Mustache.parse(rolesTemplate);

var playerTemplate = $('#voteTemplate').html();
Mustache.parse(rolesTemplate);

toastr.options = {
    "positionClass": "toast-bottom-left"
};

function RoleConfig(roleName, description) {
    this.roleName = roleName;
    this.description = description;
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

    this.render = function (totalPlayers) {
        tableRow.html(Mustache.render(playerTemplate, $.extend(Current, {
            "percentage": (Current.votes / totalPlayers) * 100,
            "barStyle": Current.votes > (totalPlayers/2)? "progress-bar-danger":"",
            "totalPlayers": totalPlayers,
            "voteDisabled": playersVotedForUser[""]?"disabled":""
        })));
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
    };

    this.playerLeftEvent = function (leavingName, totalPlayers) {
        if(leavingName === this.name) {
            tableRow.remove()
        }
        delete playersVotedForUser[leavingName];
        Current.votes = Object.keys(playersVotedForUser).length;
        this.render(totalPlayers);
    }
}

$(function () {

    var roleConfigList = [];
    var playerList = [];

    $.get('role').done(function (response) {

        $(response).each(function (i, role) {
            var roleConfig = new RoleConfig(role.name, role.description);
            roleConfigList.push(roleConfig);
            roleConfig.render();
        });
        $('[data-toggle="tooltip"]').tooltip()

    });

    function connect() {
        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function () {

            stompClient.subscribe('/user/queue/game/ended', function () {
                location.reload();
            });

            stompClient.subscribe('/user/queue/game/start', function (joinEvent) {
                if (joinEvent.body === '"CREATE"') {
                    $('#adminRow').show();
                }
                $('#joinGameRow').hide();
                $('#votesRow').show();
            });

            stompClient.subscribe('/user/queue/game/join', function (newPlayersEvent) {
                var newPlayers = JSON.parse(newPlayersEvent.body);
                $(newPlayers).each(function (i, player) {
                    newPlayer(player);
                })
            });

            stompClient.subscribe('/user/queue/player/left', function (playerLeftEvetn) {
                toastr.error(playerLeftEvetn.body + ' left the game', 'Player Left');
                playerList = $.grep(playerList, function (player) {
                    player.playerLeftEvent(playerLeftEvetn.body , playerList.length);
                    return player.name !== playerLeftEvetn.body;
                });
            });

            stompClient.subscribe('/user/queue/role/assigned', function (roleEvent) {
                var role = JSON.parse(roleEvent.body);
                toastr.success('You are a ' + role.name, 'Role Assigned');
                $('#roleNameStr').text(role.name);
                $('#roleDescriptionStr').text(role.description);
                $('#teamStr').text(role.team.name);
                $('#roleRow').show();
            });

            stompClient.subscribe('/user/queue/admin/role/assign', function (roleEvent) {
                var roles = JSON.parse(roleEvent.body);
                $(playerList).each(function (i, player) {
                    player.adminUserAssigned(roles);
                })
            });

            stompClient.subscribe('/user/queue/player/joined', function (newPlayerEvent) {
                toastr.success(newPlayerEvent.body + ' joined the game', 'Player Joined');
                newPlayer(newPlayerEvent.body);
                $.each(playerList, function (i, player) {
                    player.render(playerList.length);
                })
            });

            stompClient.subscribe('/user/queue/player/voted', function (voteEvent) {
                var vote = JSON.parse(voteEvent.body);
                toastr.warning(vote.sourcePlayer + ' voted for ' + vote.targetPlayer, 'Player Voted');
                $(playerList).each(function (i, player) {
                    player.playerVoted(vote.sourcePlayer, vote.targetPlayer);
                    player.render(playerList.length);
                })
            });

            stompClient.subscribe('/user/queue/notification', function (notificationEvent) {
                var notification = JSON.parse(notificationEvent.body);
                toastr[notification.type](notification.body, notification.title)
            });
        });

        $('#assignRolesBtn').click(function () {
            stompClient.send('/app/admin/role/assign', {}, JSON.stringify(roleConfigList));
        });

        $('#joinGameBtn').on('click', function () {
            gameRequest('/app/game/join');
        });

        $('#voteTableBody').on('click', 'button', function (e) {
            var votePlayer = $(e.target).attr('data-name');
            stompClient.send('/app/player/vote', {}, votePlayer);
            $(playerList).each(function (i, player) {
                player.playerVoted("", votePlayer);
                player.render(playerList.length);
            })
        });

        $('#createGameBtn').on('click', function () {
            gameRequest('/app/game/create');
        });

        function newPlayer(name) {
            var playerVote = new PlayerVote(name);
            playerList.push(playerVote);
            playerVote.render(playerList.length);
        }

        function gameRequest(url) {
            var gameId = $('#GameIdInput').val();
            var name = $('#NameInput').val();
            var request = JSON.stringify({name: name, id: gameId});
            stompClient.send(url, {}, request);
        }

    }

    connect();

});


