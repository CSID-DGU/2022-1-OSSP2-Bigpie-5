import os

file_path = "D:\\projects\\2022-1-OSSP2-Bigpie-5\\tts-model\\datasets\\ko\\audio"
file_names = os.listdir(file_path)

i = 1
for name in file_names:
    src = os.path.join(file_path, name)
    dst = str(i) + ".wav"
    dst = os.path.join(file_path, dst)

    print(name)

    # os.rename(src, dst)
    i += 1
