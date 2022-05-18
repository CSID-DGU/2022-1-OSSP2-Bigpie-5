f = open("D:/projects/2022-1-OSSP2-Bigpie-5/tts-model/data-preparing/text.txt", "r", encoding='UTF-8')
f_dest = open("D:/projects/2022-1-OSSP2-Bigpie-5/tts-model/data-preparing/text_new.txt", "w", encoding='UTF-8')

lines = f.readlines()

index = 2007

for i, line in enumerate(lines):
    str1 = "\"./datasets/ko/audio\\\\새로운 녹음 " + str(i+2007) + ".wav\":"
    
    f_dest.write(str1 + line[37:])

    
    