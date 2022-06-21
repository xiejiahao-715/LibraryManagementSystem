# -*- coding = utf-8 -*-
# @Author : 谢嘉豪
# @File : CheckImageTest.py
# @Software: PyCharm
from selenium.common.exceptions import JavascriptException
from selenium.webdriver.common.by import By
from selenium.common.exceptions import NoSuchElementException
import unittest
import config
from ddt import ddt, data
import time

logger = config.getGlobalLogger()


@ddt
class CheckImageTest(unittest.TestCase):
    def setUp(self):
        self.driver = config.getWebDriver()
        self.vars = {}

    def tearDown(self):
        self.driver.quit()

    @data(1,2,3)
    def testImage(self, index):
        """测试查询到的图书的封面图片是否能够正常显示
        :param index 代表查询第 index 本书的封面图书
        """
        logger.info('参数：index：%s' % index)

        self.driver.get("http://127.0.0.1:8010/login")
        self.driver.set_window_size(1048, 1020)
        logger.info('初始化打开浏览器')

        self.driver.find_element(By.XPATH, "//input").click()
        self.driver.find_element(By.XPATH, "//input").send_keys("admin")
        logger.info('输入用户名：admin')

        self.driver.find_element(By.XPATH, "//div[2]/div[2]/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[2]/div[2]/div/div/input").send_keys("admin")
        logger.info('输入用户密码：admin')

        self.driver.find_element(By.CSS_SELECTOR, ".el-button").click()
        logger.info('点击登录')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//span[contains(.,\'图书管理\')]").click()
        logger.info('展开图书管理选项菜单')

        time.sleep(0.5)

        self.driver.find_element(By.XPATH, "//li/ul/a[contains(.,\'全部图书\')]").click()
        logger.info('前往全部图书页面')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//div[contains(.,\'排序\')]/div/div/div/div/div/input").click()
        time.sleep(0.5)
        self.driver.find_element(By.XPATH, "//li[contains(.,\'按照出版日期排序\')]").click()
        self.driver.find_element(By.XPATH, "//span[contains(.,\'查询\')]").click()
        logger.info('按照出版日期的倒序筛选图书并查询')

        time.sleep(1)

        bookTitle = self.driver.find_element(By.XPATH, '//tr[%s]//td[5]/div' % index).text
        logger.info("按照出版日期倒序 排第 %s 位的图书名称：《%s》" % (index,bookTitle))

        # 查询出来的第 index 本图书的封面是否加载成功
        imgLoadSuccess = False

        try:
            img = self.driver.find_element(By.XPATH,"//tr[%s]//td[4]/div/div/img" % index)
            naturalWidth = self.driver.execute_script("return arguments[0].naturalWidth",img)
            naturalHeight = self.driver.execute_script("return arguments[0].naturalHeight",img)
            print(naturalWidth,naturalHeight)
            if naturalWidth > 0 and naturalHeight > 0:
                imgLoadSuccess = True
        except (JavascriptException,NoSuchElementException):
            imgLoadSuccess = False

        self.assertTrue(imgLoadSuccess,msg="图书《%s》的图片未加载成功" % bookTitle)

        self.driver.find_element(By.CSS_SELECTOR, ".right > .el-button").click()
        logger.info('点击退出按钮退出系统')

        self.driver.close()
        logger.info('关闭浏览器')
