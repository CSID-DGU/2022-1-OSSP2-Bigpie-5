import express from 'express';
import path from 'path';
import multer from 'multer';

const __dirname = path.resolve();

const _storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, './uploads');
  },
  filename: (req, file, cb) => {
    cb(null, file.originalname);
  },
});
const upload = multer({ storage: _storage });

const app = express();

app.get('/', (req, res) => {
  res.sendFile(__dirname + '/index.html');
});

app.post('/upload', upload.any(), (req, res) => {
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
