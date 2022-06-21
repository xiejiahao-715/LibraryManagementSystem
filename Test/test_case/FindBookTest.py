from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.common.by import By
import unittest
import config
from ddt import ddt, data
import time

logger = config.getGlobalLogger()


@ddt
class FindBookTest(unittest.TestCase):
    def setUp(self):
        self.driver = config.getWebDriver()
        self.vars = {}

    def tearDown(self):
        self.driver.quit()

    @data('百年孤独', '秋园', '生死疲劳', 'error')
    def testFindBookByTitle(self, bookTitle):
        """测试根据图书标题查找图书的功能
        :param bookTitle 需要查找图书的标题关键字(模糊匹配)
        """
        logger.info('参数：bookTitle：%s' % bookTitle)

        self.driver.get("http://127.0.0.1:8010/login")
        self.driver.set_window_size(1048, 1020)
        logger.info('初始化打开浏览器')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").send_keys("test1")
        logger.info('输入用户名：test1')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").send_keys("test1")
        logger.info('输入用户密码：test1')

        self.driver.find_element(By.CSS_SELECTOR, ".el-button").click()
        logger.info('点击登录')

        time.sleep(1)

        self.driver.find_element(By.CSS_SELECTOR, "a:nth-child(3) > .el-menu-item").click()
        logger.info('前往查找图书页面')

        time.sleep(0.5)

        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'app\']/div/div[2]/div[2]/div[3]/div/div/div/form/div[2]/div/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'app\']/div/div[2]/div[2]/div[3]/div/div/div/form/div[2]/div/div/div/input").send_keys(
            bookTitle)
        logger.info('在筛选项 标题 中输入：%s' % bookTitle)

        self.driver.find_element(By.CSS_SELECTOR, ".el-form-item__content > .el-button").click()
        logger.info('点击查询按钮')

        time.sleep(1)

        try:
            self.vars['searchBookTitle'] = self.driver.find_element(By.XPATH, '//tr[1]//td[5]/div').text
            logger.info('保存查询第一本图书的标题：%s' % self.vars['searchBookTitle'])
        except NoSuchElementException:
            self.vars['searchBookTitle'] = ''
            logger.info('无法查询到图书')

        self.assertTrue(self.vars['searchBookTitle'].find(bookTitle) != -1, '查询图书 %s 不存在' % bookTitle)

        self.driver.find_element(By.CSS_SELECTOR, ".right > .el-button").click()
        logger.info('点击退出按钮退出系统')

        self.driver.close()
        logger.info('关闭浏览器')

    @data('9787559640666', '9787544269155','不存在')
    def testFindBookByISBN(self, bookISBN):
        """测试图书的ISBN号查找图书的功能
        :param bookISBN 需要查找图书的ISBN号(全值匹配)
        """
        logger.info('参数：bookISBN：%s' % bookISBN)

        self.driver.get("http://127.0.0.1:8010/login")
        self.driver.set_window_size(1048, 1020)
        logger.info('初始化打开浏览器')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").send_keys("test1")
        logger.info('输入用户名：test1')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").send_keys("test1")
        logger.info('输入用户密码：test1')

        self.driver.find_element(By.CSS_SELECTOR, ".el-button").click()
        logger.info('点击登录')

        time.sleep(1)

        self.driver.find_element(By.CSS_SELECTOR, "a:nth-child(3) > .el-menu-item").click()
        logger.info('前往查找图书页面')

        time.sleep(0.5)

        self.driver.find_element(
            By.XPATH,
            "//div[@id='app']/div/div[2]/div[2]/div[3]/div/div/div/form/div/div/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[@id='app']/div/div[2]/div[2]/div[3]/div/div/div/form/div/div/div/div/input").send_keys(
            bookISBN)
        logger.info('在筛选项 ISBN 中输入：%s' % bookISBN)

        self.driver.find_element(By.CSS_SELECTOR, ".el-form-item__content > .el-button").click()
        logger.info('点击查询按钮')

        time.sleep(1)

        try:
            self.vars['searchBookISBN'] = self.driver.find_element(By.XPATH, '//tr[1]//td[3]/div').text
            logger.info('保存查询图书的ISBN：%s' % self.vars['searchBookISBN'])
        except NoSuchElementException:
            self.vars['searchBookISBN'] = ''
            logger.info('无法查询到图书')

        self.assertEqual(self.vars['searchBookISBN'], bookISBN, '查询图书 ISBN: %s 不存在' % bookISBN)

        self.driver.find_element(By.CSS_SELECTOR, ".right > .el-button").click()
        logger.info('点击退出按钮退出系统')

        self.driver.close()
        logger.info('关闭浏览器')
