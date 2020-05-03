const gulp = require('gulp'),
    watch = require('gulp-watch'),
    { spawn, exec } = require('child_process'),
    fs = require('fs');

let lastServerProcess = null;

gulp.task('default', () => {

    return watch('src/**/*.*', () => {

        console.log('restart initiated');

        if (lastServerProcess) {
            lastServerProcess.kill();

            console.log('previous server killed');
        }


        exec(`getent hosts ${process.env.RESOURCE_HOST} | awk '{ print $1 }'`, (error, stdout, stderror) => {

            if (error) {
                console.error(error);

                console.log(stderror);

            }
            else {
                startingApp(stdout.replace('\n', ''));
            }


        });

    });

});

function startingApp(host) {

    console.log(`building for: ${process.env.RESOURCE_HOST} (external: ${host})`);

    exec('npm install', (error, stdout, stderror) => {
        
        if(error)
        {
            console.error(error);

            return;
        }

        console.log(stdout);
        
        exec(`REACT_APP_RESOURCE_URL='${process.env.RESOURCE_PROTOCOL}://${host}:${process.env.RESOURCE_PORT}' npm run build`, (error, stdout, stderror) => {

            if (error) {

                console.error(error);

                console.log(stderror);

                return;
            }

            console.log(stdout);

            console.log('starting server');

            lastServerProcess = spawn('lite-server', ['-c', "dev-server-config.json"]);

            lastServerProcess.stdout.on('data', (data) => {
                console.log(`server:\t|${data}`);
            });

            lastServerProcess.stderr.on('data', (data) => {
                console.log(`server:\t|${data}`);
            });
        });
    })


}
