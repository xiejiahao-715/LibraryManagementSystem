# -*- coding = utf-8 -*-
# @Author : 谢嘉豪
# @File : config.py
# @Software: PyCharm

import logging
from selenium import webdriver

# 驱动路径
driver_path = r'D:\LibraryManagementSystem\Test\driver\msedgedriver.exe'
# 测试网站的基础路径
base_url = r'http://127.0.0.1:8010'

# 日志容器的名称
logger_name = 'test'


# 获取全局的驱动器
def getWebDriver():
    return webdriver.Edge(executable_path=driver_path)


# 获取全局的测试容器
def getGlobalLogger():
    return logging.getLogger(logger_name)
