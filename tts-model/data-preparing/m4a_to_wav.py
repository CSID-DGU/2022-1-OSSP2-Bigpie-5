import os
import glob

files = glob.glob("D:/projects/2022-1-OSSP2-Bigpie-5/tts-model/datasets/ko/audio/*.m4a")

for name in files:
    if not os.path.isdir(name):
        src = os.path.splitext(name)
        os.rename(name, src[0] + ".wav")

