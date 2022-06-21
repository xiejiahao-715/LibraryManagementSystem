# -*- coding = utf-8 -*-
# @Author : 谢嘉豪
# @File : AddOrDeleteBookTest.py
# @Software: PyCharm
import pytest
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
import unittest
import config
from ddt import ddt, data, unpack
import time

logger = config.getGlobalLogger()


@ddt
class AddOrDeleteBookTest(unittest.TestCase):
    def setUp(self):
        self.driver = config.getWebDriver()
        self.vars = {}

    def tearDown(self):
        self.driver.quit()

    @pytest.mark.run(order=0)
    @data(
        {'bookTitle': '用于测试的图书', 'addNum': 1},
        {'bookTitle': '用于测试的图书', 'addNum': 2}
    )
    @unpack
    def testAddBook(self, bookTitle, addNum):
        """测试添加图书的功能
        :param bookTitle 需要添加的图书名称
        :param addNum 需要添加的图书的数量
        """
        logger.info('参数：bookTitle：%s，addNum：%s' % (bookTitle, addNum))

        self.driver.get("http://127.0.0.1:8010/login")
        self.driver.set_window_size(1048, 1020)
        logger.info('初始化打开浏览器')

        self.driver.find_element(By.XPATH, "//input").click()
        self.driver.find_element(By.XPATH, "//input").send_keys("admin")
        logger.info('输入用户名：admin')

        self.driver.find_element(By.XPATH, "//div[2]/div[2]/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[2]/div[2]/div/div/input").send_keys("admin")
        logger.info('输入用户密码：admin')

        self.driver.find_element(By.CSS_SELECTOR, ".el-button > span").click()
        logger.info('点击登录')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//li/div[contains(.,\'图书管理\')]").click()
        logger.info('展开图书管理选项')

        time.sleep(0.5)

        self.driver.find_element(By.XPATH, "//li/ul/a/li[contains(.,\'全部图书\')]").click()
        logger.info('前往全部图书页面')

        time.sleep(1)

        self.driver.find_element(
            By.XPATH, "//div[contains(.,\'标题\')]/div/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[contains(.,\'标题\')]/div/div/div/input").send_keys("用于测试的图书")
        logger.info('填充搜索条件项 标题：%s' % bookTitle)

        self.driver.find_element(By.XPATH, "//button[contains(.,\'查询\')]").click()
        logger.info('点击查询按钮')

        time.sleep(1)

        # 获取查询出来的书籍的信息
        try:
            self.vars["bookTitle"] = self.driver.find_element(By.XPATH, "//tr[1]//td[5]/div").text
            self.vars["bookNum"] = int(self.driver.find_element(By.XPATH, "//tr[1]//td[10]/div").text)
        except NoSuchElementException:
            self.vars["bookTitle"] = None
            self.vars["bookNum"] = None
        except ValueError:
            self.vars["bookNum"] = None
        self.assertEquals(
            self.vars["bookTitle"], bookTitle,
            msg='图书《%s》不存在' % bookTitle)
        self.assertIsNotNone(
            self.vars["bookNum"],
            msg='无法获取当前图书《%s》的数量(可能是图书数量不为纯数字)' % bookTitle)
        self.assertTrue(self.vars["bookNum"] >= 0, msg='图书的数量必须为正整数')

        logger.info('图书《%s》添加前的数量 %s' % (bookTitle, self.vars["bookNum"]))

        self.driver.find_element(By.XPATH, "//tr[1]//td[11]//button[contains(.,\'添加图书\')]").click()
        logger.info('点击添加图书的按钮')

        time.sleep(0.5)

        self.driver.find_element(By.XPATH, "//div/div[2]/div/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div/div[2]/div/div/div/input").clear()
        self.driver.find_element(By.XPATH, "//div/div[2]/div/div/div/input").send_keys(addNum)
        logger.info("输入添加图书的数量：%s", addNum)

        self.driver.find_element(By.XPATH, "//button[contains(.,\'确定\')]").click()
        logger.info('点击确认按钮添加图书')

        time.sleep(1)

        # 获取添加后图书的数量
        self.vars["afterAddBookNum"] = int(self.driver.find_element(By.XPATH, "//tr[1]//td[10]/div").text)
        logger.info('图书《%s》添加后的数量：%s' % (bookTitle, self.vars["afterAddBookNum"]))

        self.assertEqual(self.vars["afterAddBookNum"], self.vars["bookNum"] + addNum, msg='图书添加前和添加后的数量不合理')

        self.driver.find_element(By.CSS_SELECTOR, ".right span").click()
        logger.info('退出登录')

        self.driver.close()
        logger.info('关闭浏览器')

    @pytest.mark.run(order=1)
    @data(
        {'bookTitle': '用于测试的图书', 'deleteNum': 1},
        {'bookTitle': '用于测试的图书', 'deleteNum': 2},
        {'bookTitle': '用于测试的图书', 'deleteNum': 100}
    )
    @unpack
    def testDeleteBook(self, bookTitle, deleteNum):
        """测试图书出库的功能
        :param bookTitle 需要出库的图书的名称
        :param deleteNum 需要出库的图书的数量
        """
        logger.info('参数：bookTitle：%s，deleteNum：%s' % (bookTitle, deleteNum))

        self.driver.get("http://127.0.0.1:8010/login")
        self.driver.set_window_size(1048, 1020)
        logger.info('初始化打开浏览器')

        self.driver.find_element(By.XPATH, "//input").click()
        self.driver.find_element(By.XPATH, "//input").send_keys("admin")
        logger.info('输入用户名：admin')

        self.driver.find_element(By.XPATH, "//div[2]/div[2]/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[2]/div[2]/div/div/input").send_keys("admin")
        logger.info('输入用户密码：admin')

        self.driver.find_element(By.CSS_SELECTOR, ".el-button > span").click()
        logger.info('点击登录')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//li/div[contains(.,\'图书管理\')]").click()
        logger.info('展开图书管理选项')

        time.sleep(0.5)

        self.driver.find_element(By.XPATH, "//li/ul/a/li[contains(.,\'全部图书\')]").click()
        logger.info('前往全部图书页面')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//div[contains(.,\'标题\')]/div/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[contains(.,\'标题\')]/div/div/div/input").send_keys("用于测试的图书")
        logger.info('填充搜索条件项 标题：%s' % bookTitle)

        self.driver.find_element(By.XPATH, "//button[contains(.,\'查询\')]").click()
        logger.info('点击查询按钮')

        time.sleep(1)

        # 获取查询出来的书籍的信息
        try:
            self.vars["bookTitle"] = self.driver.find_element(By.XPATH, "//tr[1]//td[5]/div").text
            self.vars["bookNum"] = int(self.driver.find_element(By.XPATH, "//tr[1]//td[10]/div").text)
        except NoSuchElementException:
            self.vars["bookTitle"] = None
            self.vars["bookNum"] = None
        except ValueError:
            self.vars["bookNum"] = None
        self.assertEquals(self.vars["bookTitle"], bookTitle, msg='图书《%s》不存在' % bookTitle)
        self.assertIsNotNone(self.vars["bookNum"], msg='无法获取当前图书《%s》的数量(可能是图书数量不为纯数字)' % bookTitle)
        self.assertTrue(self.vars["bookNum"] >= 0, msg='图书的数量必须为正整数')

        logger.info('图书《%s》出库前的数量 %s' % (bookTitle, self.vars["bookNum"]))

        self.assertTrue(self.vars["bookNum"] >= deleteNum,msg='出库数量大于图书的数量，不合法')

        self.driver.find_element(By.XPATH, "//tr[1]//td[11]//button[contains(.,\'图书出库\')]").click()
        logger.info('点击图书出库的按钮')

        time.sleep(0.5)

        self.driver.find_element(By.XPATH, "//div/div[2]/div/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div/div[2]/div/div/div/input").clear()
        self.driver.find_element(By.XPATH, "//div/div[2]/div/div/div/input").send_keys(deleteNum)
        logger.info("输入图书出库的数量：%s", deleteNum)

        self.driver.find_element(By.XPATH, "//button[contains(.,\'确定\')]").click()
        logger.info('点击确认按钮进行图书出库')

        time.sleep(1)

        # 获取图书出库后的数量
        self.vars["afterDeleteBookNum"] = int(self.driver.find_element(By.XPATH, "//tr[1]//td[10]/div").text)
        logger.info('图书《%s》出库后的数量：%s' % (bookTitle, self.vars["afterDeleteBookNum"]))

        self.assertEqual(
            self.vars["afterDeleteBookNum"], self.vars["bookNum"] - deleteNum,
            msg='图书出库前和出库后的数量不合理')

        self.driver.find_element(By.CSS_SELECTOR, ".right span").click()
        logger.info('退出登录')

        self.driver.close()
        logger.info('关闭浏览器')