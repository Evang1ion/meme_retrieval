import cv2
import pytesseract

# 设置 Tesseract OCR 的路径（如果不在系统 PATH 中）
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'

# 读取图像文件
image = cv2.imread('scripts/1.png')

# 将图像转换为灰度图像
gray_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

# 使用中文 OCR 进行文本识别
text = pytesseract.image_to_string(gray_image, lang='chi_sim')

# 输出识别结果
print("提取到的中文文字：", text)
