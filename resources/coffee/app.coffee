$ ->
    do open = ->
        window.socket = new WebSocket window.location.href.replace("https://", "ws://").replace("http://", "ws://")
        socket.onopen = ->
            console.log "socket opened"
        socket.onmessage = (msg) ->
            $("#messages").append("<p>"+msg.data+"</p>")
        socket.onclose = ->
            console.log "socket closed"
            open()
    $("form").on "submit", (e) ->
        e.preventDefault()
        socket.send $("#message").val()
        $("#message").val ""
