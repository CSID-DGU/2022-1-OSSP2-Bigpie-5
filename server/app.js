import express from 'express';
import path from 'path';
import multer from 'multer';
import util from 'util';
import child_process from 'child_process';
import { spawn } from 'child_process';
// import spawn from 'await-spawn';

const exec = util.promisify(child_process.exec);

const __dirname = path.resolve();

const _storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, '../tts-model/datasets/test/audio');
  },
  filename: (req, file, cb) => {
    cb(null, file.originalname.slice(0, file.originalname.length - 3) + 'wav');
  },
});
const upload = multer({ storage: _storage });

const app = express();

const sleep = (ms) => {
  return new Promise((resolve) => setTimeout(resolve, ms));
};

app.get('/', async (req, res) => {
  res.sendFile('D:\\projects\\2022-1-OSSP2-Bigpie-5\\server\\index.html');
});

app.post('/upload', upload.any(), async (req, res) => {
  await sleep(5000);

  const generateData = spawn('python', [
    '-m',
    'datasets.generate_data',
    './datasets/test/alignment.json',
  ]);

  generateData.stdout.on('data', (data) => {
    console.log(`stdout: ${data}`);
  });

  generateData.stderr.on('data', (data) => {
    console.error(`stderr: ${data}`);
  });

  generateData.on('close', (code) => {
    console.log(`child process exited with code ${code}`);
  });

  await sleep(10000);

  const train = spawn('python', ['train.py', '--data_path=datasets/test']);

  train.stdout.on('data', (data) => {
    console.log(`stdout: ${data}`);
  });

  train.stderr.on('data', (data) => {
    console.error(`stderr: ${data}`);
  });

  train.on('close', (code) => {
    console.log(`child process exited with code ${code}`);
  });

  //console.log(req.files);

  res.redirect('/');

  //res.json({ ok: true, data: 'Single Upload Ok' });
});

app.listen(8080, () => {
  console.log('server is running on port 8080');
});
