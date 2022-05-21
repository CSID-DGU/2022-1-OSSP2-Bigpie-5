import { spawnSync } from 'child_process';

const concatenate = spawnSync('python', ['../server/concatenateAudio.py']);

var concatenateOutput = concatenate.stdout;

console.log(String(concatenateOutput));
