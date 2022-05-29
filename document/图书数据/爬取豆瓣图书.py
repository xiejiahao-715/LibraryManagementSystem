import requests
from pyquery import PyQuery as pq
import os
import xlrd, xlwt
from xlutils.copy import copy as xl_copy
import time
import re
import random

# 需要爬取的分类
# tagList = ['文学', '算法', '推理', '科幻小说', '编程']
tagList = ['音乐']
# 爬取的豆瓣模板链接
baseUrl = 'https://book.douban.com/tag/{}?start={}&type=T'
# 每一页的大小
pagesize = 20
header = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.54 Safari/537.36 Edg/101.0.1210.39'
}


# 获取网页源代码
def get_html(tag, start):
    url = baseUrl.format(tag, start)
    print('爬取的豆瓣图书链接：' + url)
    response = requests.get(url=url, headers=header).text
    return response


# 获取豆瓣图书的isbn号
def get_book_isbn(href):
    detail_dom = pq(requests.get(url=href, headers=header).text)
    isbnStr = str(detail_dom("#info>span").filter(":contains('ISBN:')")).strip()
    return re.match('^<span.*?>.*?</span>(.*?)$', isbnStr).group(1).strip()


# 解析获取的网页，封装进list
def get_book_info_list(html):
    # 获取dom对象
    dom = pq(html)
    # 获取存储图书信息的li标签
    items = dom.items('#subject_list>.subject-list>li.subject-item')
    book_info_list = []
    for item in items:
        isbn_href = item('.pic>a').attr('href')
        isbn = get_book_isbn(isbn_href)
        # 作者/出版社/出版时间/价格信息
        publish = item('.info>.pub').text().split('/')
        publish_info_size = len(publish)
        if publish_info_size >= 4:
            # 图书价格
            price = publish[publish_info_size - 1].strip()
            # 出版时间
            publish_time = publish[publish_info_size - 2].strip()
            # 出版社
            publishing_house = publish[publish_info_size - 3].strip()
            author = ''
            for i in range(0, publish_info_size - 4 + 1):
                author += publish[i].strip()
                if i != publish_info_size - 4:
                    author += '/'
            # 图书作者
            author = author.strip()
            # 图片链接
            img_url = item('.pic img').attr('src')
            # 图书标题
            title = item('.info>h2 a').attr('title')
            # 图书简介
            desc = item('.info>p').text()
            num = random.randint(0, 6)
            # 将信息按顺序加入列表
            book_info = [title, author, publishing_house, img_url, publish_time, desc, price, isbn, num]
            book_info_list.append(book_info)
            print(book_info)
        time.sleep(1)
    return book_info_list


# 将图书信息list写入excel表格文件中
def write_book_info_list_to_xlsx(filename, book_info_list, tag):
    workbook = None
    # 查看该目录下是否存在对应的文件，有则打开
    if os.path.exists(filename):
        xls = xlrd.open_workbook(filename)
        workbook = xl_copy(xls)
    else:
        # 不存在则创建工作表
        workbook = xlwt.Workbook(encoding="utf-8")
    try:
        # 如果工作表存在则删除
        sheet = workbook.get_sheet(tag)
        sheet.remove()
    except:
        pass
    # 添加工作表
    sheet = workbook.add_sheet(tag, cell_overwrite_ok=True)
    # 写入列名
    col = ('标题', '作者', '出版社', '图片链接', '出版时间', '图书简介', '图书价格', 'ISBN', '数量')
    for i in range(0, len(col)):
        sheet.write(0, i, col[i])
    # 写入数据
    for i in range(0, len(book_info_list)):
        book_info = book_info_list[i]
        for j in range(0, len(book_info)):
            sheet.write(i + 1, j, book_info[j])
    workbook.save(filename)


def start(filename):
    for tag in tagList:
        html = get_html(tag=tag, start=0)
        book_list = get_book_info_list(html=html)
        write_book_info_list_to_xlsx(filename=filename, book_info_list=book_list, tag=tag)
        # 休眠3秒，降低频率
        time.sleep(3)


if __name__ == "__main__":
    file = '豆瓣图书表.xls'
    if os.path.exists(file):
        os.remove(file)
    start(filename=file)
