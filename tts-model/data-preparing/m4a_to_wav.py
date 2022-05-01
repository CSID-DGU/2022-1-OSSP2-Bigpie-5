import os
import glob

files = glob.glob("C:/Users/Seungheon/Desktop/dataset/Data/*.m4a")

for name in files:
    if not os.path.isdir(name):
        src = os.path.splitext(name)
        os.rename(name, src[0] + ".wav")

