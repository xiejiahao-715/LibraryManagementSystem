# -*- coding = utf-8 -*-
# @Author : 谢嘉豪
# @File : run_test.py
# @Software: PyCharm

import unittest
from MyBeautifulReport import MyBeautifulReport
import time

if __name__ == '__main__':
    suite_tests = unittest.defaultTestLoader.discover(start_dir='test_case', pattern="*Test.py", top_level_dir=None)
    description = '图书管理系统测试报告'
    # 取前面时间
    now = time.strftime("%Y年%m月%d日%H时%M分%S秒", time.localtime(time.time()))
    filename = '%s-%s.html' % (description, now)
    # filename = '%s.html' % description
    result = MyBeautifulReport(suite_tests)
    result.report(filename=filename, description=description, report_dir='./report', theme='theme_candy')
