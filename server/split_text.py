text = "컴퓨터 공학은 컴퓨터 하드웨어 및 소프트웨어를 개발하는 데 필요한 전기공학 및 컴퓨터 과학의 여러 가지 분야를 통합하는 학문 분과이다. 현대 정보화 사회에서 컴퓨터의 하드웨어와 소프트웨어를 연구, 컴퓨터 시스템과 컴퓨터 관련 기술을 개발하여 익히고 이를 각 분야에 응용함을 목적으로 한다.하드웨어 부분에서 전자기학 이론 분야와 마이크로프로세서 분야, 전자 회로 분야로 나뉘고, 소프트웨어 부분에서 컴퓨터 과학 이론 분야와 컴퓨터 프로그래밍 분야, 컴퓨터 시스템 분야 등으로 나뉜다. 컴퓨터 과학과 전자공학이 연계되는 학문으로, 컴퓨터를 비롯한 전자제품이나 로봇 공학 등, 하드웨어와 소프트웨어 지식이 둘 다 필요한 분야들이 대표적이다."

print(len(text))

texts = []

# for i in range(0, len(text), 45):
#     texts.append(text[i:i+45])

# texts = text.split(".")

first = 0
finished = False

while first < len(text):
    last = first + 35

    for i in range(last, last + 20):
        if i >= len(text):
            finished = True
            break
        elif text[i] == " ":
            last = i
            break
    
    if finished:
        texts.append(text[first:])
    else:
        texts.append(text[first:last])   
    
    first = last + 1      
    
print(texts)