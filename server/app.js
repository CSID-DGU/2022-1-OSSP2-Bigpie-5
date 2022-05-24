import express from 'express';
import path from 'path';
import multer from 'multer';
import util from 'util';
import child_process from 'child_process';
import { spawnSync } from 'child_process';
import mime from 'mime';
import fs from 'fs';
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
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

const sleep = (ms) => {
  return new Promise((resolve) => setTimeout(resolve, ms));
};

app.get('/', async (req, res) => {
  res.sendFile('D:\\projects\\2022-1-OSSP2-Bigpie-5\\server\\index.html');
});

app.post('/voice', async (req, res) => {
  let { text } = req.body;

  console.log(text);

  const texts = [];

  let first = 0;
  let finished = false;

  while (first < text.length) {
    let last = first + 35;

    for (let i = last; i < last + 20; i++) {
      if (i >= text.length) {
        finished = true;
        break;
      } else if (text.charAt(i) == ' ') {
        last = i;
        break;
      }
    }

    if (finished) {
      texts.push(text.substring(first));
    } else {
      texts.push(text.substring(first, last));
    }

    first = last + 1;
  }

  console.log(texts);

  const filepaths = [];

  const numFiles = texts.length;

  for (const text of texts) {
    let wavPath = '';

    const synthesize = spawnSync('python', [
      'synthesizer.py',
      '--load_path',
      'logs/ko_single',
      '--text',
      `"${text}"`,
    ]);

    const savedOutput = synthesize.stdout;

    wavPath = String(savedOutput);

    console.log(wavPath);

    wavPath = wavPath.slice(wavPath.indexOf('samples'));

    wavPath = wavPath.substring(0, wavPath.indexOf('\r'));

    const filepath = `D:/projects/2022-1-OSSP2-Bigpie-5/tts-model/${wavPath}`;

    console.log(filepath);

    filepaths.push(filepath);
  }

  const concatenate = spawnSync('python', [
    '../server/concatenateAudio.py',
    numFiles,
  ]);

  const concatenateOutput = concatenate.stdout;

  console.log(String(concatenateOutput));

  fs.readFile(
    'D:/projects/2022-1-OSSP2-Bigpie-5/server/voices/result.wav',
    (err, data) => {
      if (err) {
        console.log(err);
        res.end(null);
      } else {
        console.log(filepaths);

        res.end(data);
      }
    }
  );
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
    console.log(`generateData exited with code ${code}`);
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
    console.log(`train exited with code ${code}`);
  });

  res.redirect('/');
});

app.listen(8080, () => {
  console.log('server is running on port 8080');
});
