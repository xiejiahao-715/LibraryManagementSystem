# -*- coding = utf-8 -*-
# @Author : 谢嘉豪
# @File : MyBeautifulReport.py
# @Software: PyCharm

# 为测试报告中添加日志记录的功能
from io import StringIO as StringIO
from BeautifulReport import BeautifulReport
from config import getGlobalLogger
import logging
import sys


class MyBeautifulReport(BeautifulReport):
    def __init__(self, suites):
        super().__init__(suites)

        # 获取全局日志容器
        self.logger = getGlobalLogger()
        # 设置日志级别
        self.logger.setLevel(logging.INFO)

        self.log_cap = None
        self.reportHandler = None
        self.consoleHandler = None

        # 日志的格式
        self.formatter = logging.Formatter('%(asctime)s - %(name)s - "%(filename)s: %(lineno)d" - %(funcName)s - %(levelname)s - %(message)s')

    def startTest(self, test) -> None:
        """当测试用例即将运行时调用"""

        # 初始化日志处理器，记录到内存
        self.log_cap = StringIO()

        # 用于记录日志信息到测试报告中的处理器
        self.reportHandler = logging.StreamHandler(self.log_cap)
        self.reportHandler.setLevel(logging.INFO)
        self.reportHandler.setFormatter(self.formatter)
        self.logger.addHandler(self.reportHandler)

        # 用于记录日志信息到控制台的处理器
        self.consoleHandler = logging.StreamHandler(sys.stdout)
        self.consoleHandler.setLevel(logging.INFO)
        self.consoleHandler.setFormatter(self.formatter)
        self.logger.addHandler(self.consoleHandler)

        # 执行测试用例
        super().startTest(test)

    def complete_output(self):
        # 加上内存中的日志内容
        return self.log_cap.getvalue()

    def stopTest(self, test) -> None:
        super().stopTest(test)

        # 清除logger的处理器
        self.logger.removeHandler(self.reportHandler)
        self.logger.removeHandler(self.consoleHandler)