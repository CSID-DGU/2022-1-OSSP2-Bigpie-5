f = open("D:/projects/2022-1-OSSP2-Bigpie-5/tts-model/datasets/ko/alignment4.txt", "r")

f_dest = open("D:/projects/2022-1-OSSP2-Bigpie-5/tts-model/datasets/ko/alignment5.txt", "w")

lines = f.readlines()


for i, line in enumerate(lines):
    # line.replace("myset", "ko")

    if i >= 0 and i < 37:
        f_dest.write(line)

    elif i >= 37:
        if i - 35 >= 601:

            s1 = line[0:22]
            index = line.find(".", 2)

            result = s1 + "새로운 녹음 " + str(i - 34) + line[index:]

            # print(s1, s2)

            # line = line.replace("myset", "ko")

            f_dest.write(result)

        else:

            s1 = line[0:22]
            index = line.find(".", 2)

            result = s1 + "새로운 녹음 " + str(i - 35) + line[index:]

            # print(s1, s2)

            # line = line.replace("myset", "ko")

            f_dest.write(result)

    # elif i >= 103:
    #     s1 = line[0:22]
    #     index = line.find(".", 2)

    #     result = s1 + "새로운 녹음 " + str(i - 36) + line[index:]

    #     # print(s1, s2)

    #     # line = line.replace("myset", "ko")

    #     f_dest.write(result)

