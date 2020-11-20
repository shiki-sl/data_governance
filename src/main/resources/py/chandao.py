#!/usr/bin/env python3

import requests
from bs4 import BeautifulSoup
import hashlib
from lxml import html
import sys

def getCookies(username, password,loginUrl):
    loginPage = requests.get(loginUrl)
    loginPage.encoding = 'utf-8'
    SID = loginPage.cookies['zentaosid']
    print('SID = ' + SID)
    loginTree = html.fromstring(loginPage.text)
    verifyRand = loginTree.xpath('//*[@id="verifyRand"]')
    if verifyRand:
        verifyRand = verifyRand[0].attrib['value']
    print('verifyRand = ' + verifyRand)
    hl = hashlib.md5()
    hl.update(password.encode(encoding='utf-8'))
    print('Md5 第一次加密结果 = ' + hl.hexdigest())
    passwordResult = hl.hexdigest() + verifyRand
    print("passwordResult=" + passwordResult)
    hlLast = hashlib.md5()
    hlLast.update(passwordResult.encode(encoding='utf-8'))
    print('Md5 第二次加密结果 = ' + hlLast.hexdigest())
    loginBody = {"account": username, "password": hlLast.hexdigest(), "keepLogin[]": "on"}
    loginCookies = dict(zentaosid=SID, lang='zh-cn', keepLogin='on')
    loginResultPage = requests.post(loginUrl, data=loginBody, cookies=loginCookies)
    print('loginResultPage = ' + loginResultPage.text)
    token = loginResultPage.cookies
    return token


def getUpdateSqlMd(url, cookies):
    html = requests.get(url, cookies=cookies).text
    return BeautifulSoup(html, 'lxml').textarea.text


if __name__ == '__main__':
    cookies = getCookies(sys.argv[1], sys.argv[2],sys.argv[3])
    print(getUpdateSqlMd(sys.argv[3], cookies))
