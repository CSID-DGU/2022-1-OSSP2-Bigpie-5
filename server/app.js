import express from 'express';
import path from 'path';
import multer from 'multer';
import util from 'util';
import child_process from 'child_process';
import { spawn } from 'child_process';
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

  // text = '이것은 실화입니다.';
  let wavPath;

  const synthesize = spawn('python', [
    'synthesizer.py',
    '--load_path',
    'logs/ko_single',
    '--text',
    `"${text}"`,
  ]);

  synthesize.stdout.on('data', (data) => {
    console.log(`stdout: ${data}`);

    if (data.indexOf('.wav') != -1) {
      wavPath = data.toString();

      wavPath = wavPath.slice(wavPath.indexOf('samples'));

      console.log();
      console.log('yayyyyyyyyyyyyyy');
    }
  });

  synthesize.stderr.on('data', (data) => {
    console.error(`stderr: ${data}`);
  });

  synthesize.on('close', (code) => {
    console.log(`synthesize exited with code ${code}`);
  });

  await sleep(30000);

  wavPath = wavPath.substr(0, wavPath.length - 2);

  const filepath = `D:/projects/2022-1-OSSP2-Bigpie-5/tts-model/${wavPath}`;

  fs.readFile(filepath, (err, data) => {
    if (err) {
      console.log(err);
      res.end(null);
    } else {
      console.log(filepath);
      //console.log(data);

      res.end(data);
    }
  });

  // const mimetype = mime.getType(file);

  // res.setHeader('Content-disposition', 'attachment; filename=' + file);
  // res.setHeader('Content-type', mimetype);

  // const filestream = fs.createReadStream(file);

  // filestream.pipe(res);

  // res.download(filepath, 'synthesized_voice.wav');

  // res.json({ text });
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

  //console.log(req.files);

  res.redirect('/');

  //res.json({ ok: true, data: 'Single Upload Ok' });
});

app.listen(8080, () => {
  console.log('server is running on port 8080');
});
