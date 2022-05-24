import wave
import sys

numFiles = sys.argv[1]

print(numFiles)

infiles = []

last_line = 0

with open('D:\\projects\\2022-1-OSSP2-Bigpie-5\\tts-model\\wav_count.txt', 'r') as f:
        last_line = int(f.readlines()[-1][:-1])
        print("last_line1:", last_line)

last_line = last_line - int(numFiles)
print("last_line: ", last_line)



for i in range(0, int(numFiles)):
    print(infiles);
    
    infiles.append("D:\\projects\\2022-1-OSSP2-Bigpie-5\\tts-model\\samples\\" + str(last_line + i) + ".manual.wav")
    print(infiles);
    

outfile = "D:\\projects\\2022-1-OSSP2-Bigpie-5\\server\\voices\\result.wav"

data= []
for infile in infiles:
    w = wave.open(infile, 'rb')
    data.append( [w.getparams(), w.readframes(w.getnframes())] )
    w.close()
    
output = wave.open(outfile, 'wb')
output.setparams(data[0][0])
for i in range(len(data)):
    output.writeframes(data[i][1])
output.close()