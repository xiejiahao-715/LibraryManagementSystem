import unittest
from selenium.webdriver.common.by import By
import config
from ddt import ddt, data
import time

logger = config.getGlobalLogger()


@ddt
class UpdateBookInfoTest(unittest.TestCase):
    def setUp(self):
        self.driver = config.getWebDriver()
        self.vars = {}

    def tearDown(self):
        self.driver.quit()

    @data('5元','15元','20元')
    def testUpdateFirstBookPrice(self, newPrice):
        """测试修改查询到的第一本图书的价格
        :param newPrice 修改后的图书价格
        """
        logger.info('参数: newPrice: %s' % newPrice)

        self.driver.get("http://127.0.0.1:8010/login")
        self.driver.set_window_size(1048, 1020)
        logger.info('初始化打开浏览器')

        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'pane-login\']/form/div/div[2]/div/div/input").send_keys("admin")
        logger.info('输入用户名：admin')

        self.driver.find_element(By.XPATH, "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").click()
        self.driver.find_element(
            By.XPATH,
            "//div[@id=\'pane-login\']/form/div[2]/div[2]/div/div/input").send_keys("admin")
        logger.info('输入用户密码：admin')

        self.driver.find_element(By.CSS_SELECTOR, ".el-button > span").click()
        logger.info('点击登录')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//div[@id='app']/div/div[2]/div/div/div/div/ul/li/div").click()
        logger.info('点击图书管理菜单栏')

        time.sleep(0.5)

        self.driver.find_element(By.XPATH, "//div[@id=\'app\']/div/div[2]/div/div/div/div/ul/li/ul/a/li").click()
        logger.info('前往全部图书管理页面')

        time.sleep(1)

        self.vars["price"] = self.driver.find_element(By.XPATH, "//tr[1]//td[8]/div").text
        logger.info('当前查询出的第一本图书价格：%s' % self.vars['price'])

        self.driver.find_element(By.XPATH, "//tr[1]//td[11]//button[contains(.,'修改图书基本信息')]").click()
        logger.info('点击修改图书基本信息按钮前往相关页面')

        time.sleep(1)

        self.driver.find_element(By.XPATH, "//div[8]/div[2]/div/div/input").click()
        self.driver.find_element(By.XPATH, "//div[8]/div[2]/div/div/input").clear()
        self.driver.find_element(By.XPATH, "//div[8]/div[2]/div/div/input").send_keys(newPrice)
        logger.info('清空输入框输入修改后的图书价格:%s' % newPrice)

        self.driver.find_element(By.CSS_SELECTOR, ".el-button--primary > span").click()
        logger.info('点击修改按钮进行修改')

        time.sleep(1)

        self.driver.find_element(By.XPATH,"//button[contains(.,'查询')]").click()
        logger.info('点击查询按钮更新数据')

        time.sleep(0.5)

        self.vars["newPrice"] = self.driver.find_element(By.XPATH, "//tr[1]//td[8]/div").text
        logger.info('查询修改后的图书价格：%s' % self.vars['newPrice'])

        self.assertEqual(newPrice, self.vars['newPrice'], '修改图书价格失败')

        self.driver.find_element(By.CSS_SELECTOR, ".right span").click()
        logger.info('点击退出按钮退出系统')

        self.driver.close()
        logger.info('关闭浏览器')


if __name__ == '__main__':
    unittest.main()
