# -*- coding = utf-8 -*-
# @Author : 谢嘉豪
# @File : BookOperateTest.py
# @Software: PyCharm
import pytest
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
import unittest
import config
from ddt import ddt, data
import time

logger = config.getGlobalLogger()


@ddt
class BookOperateTest(unittest.TestCase):
    def setUp(self):
        self.driver = config.getWebDriver()
        self.vars = {}

    def tearDown(self):
        self.driver.quit()

    @pytest.mark.run(order=0)
    @data('文学课', '生死疲劳', '活着')
    def testBorrowBookTest(self, bookTitle):
        """测试借阅图书的功能
        :param bookTitle 想要借阅图书的名称
        """
        logger.info('参数：bookTitle：%s' % bookTitle)

        self.driver.get("http://127.0.0.1:8010/login")
        self.driver.set_window_size(1048, 1020)
        logger.info('初始化打开浏览器')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").send_keys("test1")
        logger.info('输入用户名：test1')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").send_keys(
            "test1")
        logger.info('输入用户密码：test1')

        self.driver.find_element(By.CSS_SELECTOR, ".el-button > span").click()
        logger.info('点击登录')

        time.sleep(1)

        self.driver.find_element(By.CSS_SELECTOR, "a:nth-child(3) > .el-menu-item").click()
        logger.info('前往查找书籍页面')

        time.sleep(0.5)

        self.driver.find_element(By.XPATH, "//form//div[2]/div/div/div/input").click()
        self.driver.find_element(By.XPATH, "//form//div[2]/div/div/div/input").send_keys(bookTitle)
        logger.info('填充搜索条件项 标题：%s' % bookTitle)

        self.driver.find_element(By.XPATH, "//div[5]/div/button").click()
        logger.info("点击查询按钮查找图书")

        time.sleep(0.5)

        self.vars["beforeBorrow"] = int(self.driver.find_element(By.XPATH, "//tr[1]//td[10]/div").text)
        logger.info("图书《%s》借阅前的数量：%s" % (bookTitle, self.vars["beforeBorrow"]))

        self.driver.find_element(By.XPATH, "//tr[1]//td[11]/div/div/button[contains(.,'借阅')]").click()
        logger.info('点击借阅按钮')

        time.sleep(0.2)

        self.driver.find_element(By.XPATH, "//button[contains(.,\'确定\')]").click()
        logger.info('点击确定按钮确认借阅')

        self.driver.find_element(By.CSS_SELECTOR, ".main").click()
        logger.info('关闭对话框')

        time.sleep(0.5)

        self.vars["afterBorrowNum"] = int(self.driver.find_element(By.XPATH, "//tr[1]//td[10]/div").text)
        logger.info('图书《%s》借阅后的数量：%s' % (bookTitle, self.vars["afterBorrowNum"]))

        self.assertEqual(self.vars["beforeBorrow"], self.vars["afterBorrowNum"] + 1, msg='借阅失败，不可借阅同一本书')

        self.driver.find_element(By.CSS_SELECTOR, ".right span").click()
        logger.info('退出登录')

        self.driver.close()
        logger.info('关闭浏览器')

    @pytest.mark.run(order=1)
    @data('文学课', '生死疲劳', '活着')
    def testReturnBook(self, bookTitle):
        """测试归还图书的功能
        :param bookTitle 想要归还图书的名称
        """
        logger.info('参数：bookTitle：%s' % bookTitle)

        self.driver.get("http://127.0.0.1:8010/login")
        self.driver.set_window_size(1048, 1020)
        logger.info('初始化打开浏览器')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").send_keys("test1")
        logger.info('输入用户名：test1')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").send_keys(
            "test1")
        logger.info('输入用户密码：test1')

        self.driver.find_element(By.CSS_SELECTOR, ".el-button > span").click()
        logger.info('点击登录')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//span[contains(.,'订单管理')]").click()
        logger.info('点击订单管理菜单栏')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//li/ul/a/li").click()
        logger.info('前往借阅详情界面')

        time.sleep(1)

        # 获取 借阅图书的记录
        borrowRecord = None
        try:
            borrowRecord = self.driver.find_element(
                By.XPATH,
                "//tr/child::td[3]/self::td[contains(.,'%s')]/parent::"
                "tr/child::td[7]/self::td[contains(.,'未完成')]/parent::tr" % bookTitle)
        except NoSuchElementException:
            borrowRecord = None

        self.assertIsNotNone(borrowRecord,'无图书《%s》的借阅记录' % bookTitle)

        # 获取订单号
        borrowRecordId = self.driver.find_element(
            By.XPATH,
            "//tr/child::td[3]/self::td[contains(.,'%s')]/parent::"
            "tr/child::td[7]/self::td[contains(.,'未完成')]/parent::"
            "tr//td[2]" % bookTitle).text
        logger.info('当前图书借阅订单的id：%s' % borrowRecordId)

        borrowRecord.find_element(
            By.XPATH,
            "//tr/child::td[3]/self::td[contains(.,'%s')]/parent::"
            "tr/child::td[7]/self::td[contains(.,'未完成')]/parent::"
            "tr//td[9]//span[contains(.,'归还图书')]" % bookTitle).click()
        logger.info('点击归还图书按钮')

        time.sleep(1)

        self.driver.find_element(By.XPATH,"//button[contains(.,'确定')]").click()
        logger.info('确认归还图书')

        time.sleep(1)

        # 判断归还图书是否成功
        isSuccess = False
        try:
            self.driver.find_element(
                By.XPATH,
                "//tr/child::td[2]/self::td[contains(.,'%s')]/parent::"
                "tr/child::td[8]/self::td[contains(.,'订单完成')]/parent::tr" % borrowRecordId)
            isSuccess = True
        except NoSuchElementException:
            isSuccess = False

        self.assertTrue(isSuccess,'归还图书失败')

        self.driver.find_element(By.CSS_SELECTOR, ".right span").click()
        logger.info('退出登录')

        self.driver.close()
        logger.info('关闭浏览器')
