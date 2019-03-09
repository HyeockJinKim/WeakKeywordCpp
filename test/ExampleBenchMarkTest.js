function execute(command) {
    const exec = require('child_process').exec

    exec(command, (err, stdout, stderr) => {
        process.stdout.write(stdout)
    })
}
function timer() {
    console.time('timer')
    for (let i = 0; i < 10000; ++i)
        execute('./test')
    console.timeEnd('timer')
}

function compile(filename) {
    console.time('timer')
    for (let i = 0; i < 10000; ++i)
        execute('g++ -o ./test '+filename)
    console.timeEnd('timer')
}

function origin(filename) {
    console.log(filename+":: Compile ")
    compile('../example/'+filename)
    console.log('Origin '+filename+"::")
    timer()
    console.log()
}
function converted(filename) {
    console.log(filename+":: Compile ")
    compile('../test_out/example/'+filename)
    console.log(filename+"::")
    timer()
    console.log()
}
function TestBenchMark(filename) {
    origin(filename)
    converted(filename)
}

TestBenchMark('access_specifier_base.cpp')


