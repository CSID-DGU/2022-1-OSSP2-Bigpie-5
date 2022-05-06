import express from 'express';
import path from 'path';
import multer from 'multer';
import util from 'util';
import child_process from 'child_process';

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
  // const { stdout, stderr } = await exec('pwd');

  // if (stderr) {
  //   console.error(`error: ${stderr}`);
  // }
  // console.log(`Number of files ${stdout}`);

  res.sendFile(
    'C:\\Users\\Seungheon\\Desktop\\학교자료\\3-1\\공개SW프로젝트\\projects\\2022-1-OSSP2-Bigpie-5\\server\\index.html'
  );
});

app.post('/upload', upload.any(), async (req, res) => {
  await sleep(5000);

  {
    const { stdout, stderr } = await exec(
      'python -m datasets.generate_data ./datasets/test/alignment.json'
    );

    if (stderr) {
      console.error(`stderr: ${stderr}`);
    }
    console.log(`stdout: ${stdout}`);
  }

  {
    const { stdout, stderr } = await exec(
      'python train.py --data_path=datasets/test'
    );

    if (stderr) {
      console.error(`stderr: ${stderr}`);
    }
    console.log(`stdout: ${stdout}`);
  }

  // const {
  //   fieldname,
  //   originalname,
  //   encoding,
  //   mimetype,
  //   destination,
  //   filename,
  //   path,
  //   size,
  // } = req.file;
  // const { name } = req.body;

  // console.log('body 데이터 : ', name);
  // console.log('폼에 정의된 필드명 : ', fieldname);
  // console.log('사용자가 업로드한 파일 명 : ', originalname);
  // console.log('파일의 엔코딩 타입 : ', encoding);
  // console.log('파일의 Mime 타입 : ', mimetype);
  // console.log('파일이 저장된 폴더 : ', destination);
  // console.log('destinatin에 저장된 파일 명 : ', filename);
  // console.log('업로드된 파일의 전체 경로 ', path);
  // console.log('파일의 바이트(byte 사이즈)', size);

  console.log(req.files);

  res.redirect('/');

  //res.json({ ok: true, data: 'Single Upload Ok' });
});

app.listen(8080, () => {
  console.log('server is running on port 8080');
});
