(
    
    () => {
        
        const urlParams = new URLSearchParams(window.location.search);
        const streamUrlBase64 = urlParams.get('stream');
        const streamUrl = window.atob(streamUrlBase64)

        console.log(streamUrl);

        new JSMpeg.Player(
            streamUrl,
            {
                canvas: document.getElementById("video")
            }
        )

    }

)();