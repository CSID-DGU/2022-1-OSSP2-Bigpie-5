import express from 'express';
import fs from 'fs';
import multer from 'multer';
import { spawn } from 'child_process';
import { spawnSync } from 'child_process';
import { ServerApiVersion } from 'mongodb';
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import helmet from 'helmet';
import bcrypt from 'bcrypt';
import cors from 'cors';
import morgan from 'morgan';
import fileUpload from 'express-fileupload';

const corsOptions = {
  origin: '*',
  credentials: true,
  optionSuccessStatus: 200,
};

// dotenv.config({ path: 'D:/projects/2022-1-OSSP2-Bigpie-5/server/.env' });

// const SECRET_KEY = process.env.SECRET_KEY;

// const uri = `mongodb+srv://${process.env.DB_USER}:${process.env.DB_PASSWORD}@cluster0.qq1ux.mongodb.net/myFirstDatabase?retryWrites=true&w=majority`;

// mongoose.connect(uri, {
//   useNewUrlParser: true,
//   useUnifiedTopology: true,
//   serverApi: ServerApiVersion.v1,
//   dbName: 'OSSP',
// });

// const UserSchema = mongoose.Schema({
//   email: String,
//   password: String,
// });

// const User = mongoose.model('users', UserSchema);

let fileIndex = 1;

const _storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, '../tts-model/datasets/test/audio');
  },

  filename: (req, file, cb) => {
    cb(null, fileIndex + '.wav');
    fileIndex++;
  },
});

const upload = multer({ storage: _storage });

const app = express();

app.use(
  fileUpload({
    createParentPath: true,
  })
);

app.use(cors(corsOptions));
app.use(helmet());
app.use(express.json());
app.use(morgan('tiny'));
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
  let last = 0;
  let finished = false;

  while (first < text.length && last < text.length) {
    last = first + 1;
    let found = false;

    let cnt = 0;

    for (let i = last; i < last + 30; i++) {
      cnt++;

      if (cnt >= 5) {
        if (i >= text.length) {
          finished = true;
          found = true;
          break;
        } else if (text.charAt(i) == '.') {
          last = i;
          found = true;
          break;
        } else if (text.charAt(i) == ',') {
          last = i;
          found = true;
          break;
        } else if (
          i + 1 < text.length &&
          text.charAt(i) == '서' &&
          text.charAt(i + 1) == ' '
        ) {
          last = i + 1;
          found = true;
          break;
        } else if (
          i + 1 < text.length &&
          text.charAt(i) == '고' &&
          text.charAt(i + 1) == ' '
        ) {
          last = i + 1;
          found = true;
          break;
        } else if (
          i + 2 < text.length &&
          text.charAt(i) == '하' &&
          text.charAt(i + 1) == '고' &&
          text.charAt(i + 2) == ' '
        ) {
          last = i + 2;
          found = true;
          break;
        }
      }
    }

    if (!found) {
      last = first + 30;

      for (let i = last; i < last + 30; i++) {
        if (i >= text.length) {
          finished = true;
          break;
        } else if (text.charAt(i) == ' ') {
          last = i;
          break;
        }
      }
    }

    console.log(first, last, text[first], text[last]);

    if (finished) {
      const result = text.substring(first);
      texts.push(result);

      if (
        result.length >= 2 &&
        result[result.length] == text[text.length] &&
        result[result.length - 1] == text[text.length - 1] &&
        result[result.length - 2] == text[text.length - 2]
      ) {
        break;
      }
    } else {
      const result = text.substring(first);
      texts.push(text.substring(first, last));

      // if (
      //   result[result.length] == text[text.length] &&
      //   result[result.length - 1] == text[text.length - 1] &&
      //   result[result.length - 2] == text[text.length - 2]
      // ) {
      //   break;
      // }
    }

    first = last + 1;
  }

  console.log(texts);

  const filepaths = [];

  const numFiles = texts.length;

  for (const text of texts) {
    let wavPath = '';

    console.log('여기');

    const synthesize = spawnSync('python', [
      'synthesizer.py',
      '--load_path',
      'logs/ko_single3',
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

let cnt_file = 0;
let N = 7;

app.post('/upload2', async (req, res) => {
  try {
    if (!req.files) {
      res.send({
        status: false,
        message: '파일 업로드 실패',
      });
    } else {
      cnt_file++;
      console.log(req.files);
      let f = req.files.uploadFile;
      f.mv('./datasets/test/audio/' + cnt_file + '.wav');
      res.send({
        status: true,
        message: '파일이 업로드 되었습니다.',
        data: {
          name: f.name,
          minetype: f.minetype,
          size: f.size,
        },
      });
    }
  } catch (err) {
    res.status(500).send(err);
  }
});

app.post('/upload', upload.any(), async (req, res) => {
  cnt_file++;

  console.log(req.file);

  if (cnt_file == N) {
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

    res.json({
      message: 'TTS model has started training',
    });
  } else {
    res.json({
      message: '.wav file successfully uploaded',
    });
  }
});

let users = [];
// 예시) email: bigpie@naver.com
//      password: bigpie

app.post('/signup', async (req, res) => {
  const { email, password } = req.body;

  const saltRounds = 10;
  const encrypted = await bcrypt.hash(password, saltRounds);

  let found = false;
  users.forEach((user) => {
    if (user.email == email) {
      res.json({ message: 'existing email' });
      found = true;
    }
  });

  if (!found) {
    users.push({
      email,
      password: encrypted,
    });

    res.json({ message: 'success' });
  }
});

app.post('/login', async (req, res) => {
  const { email, password } = req.body;

  users.forEach(async (user) => {
    if (user.email == email) {
      const found = await bcrypt.compare(password, user.password);
      if (found) {
        res.json({ id: email.substring(0, email.indexOf('@')) });
      } else {
        res.json({ message: 'wrong password' });
      }
    } else {
      res.json({ message: 'wrong email' });
    }
  });
});

// app.post('/signup', async (req, res) => {
//   const { email, password } = req.body;

//   const saltRounds = 10;
//   const encrypted = await bcrypt.hash(password, saltRounds);

//   User.findOne(
//     {
//       email,
//     },
//     (err, user) => {
//       if (user) {
//         res.json({ message: 'existing email' });
//       } else {
//         const newUser = new User({ email, password: encrypted });
//         newUser.save().then(() => {
//           res.json({ message: 'success' });
//         });
//       }
//     }
//   );
// });

// app.post('/login', async (req, res) => {
//   const { email, password } = req.body;

//   User.findOne(
//     {
//       email,
//     },
//     async (err, user) => {
//       if (!user) {
//         res.json({ message: 'wrong email' });
//       } else {
//         const found = await bcrypt.compare(password, user.password);

//         if (found) {
//           res.json({ id: email.substring(0, email.indexOf('@')) });
//         } else {
//           res.json({ message: 'wrong password' });
//         }
//       }
//     }
//   );
// });

app.listen(8080, () => {
  console.log('server is running on port 8080');
});
