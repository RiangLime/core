# core

---

### Intro
core模块为业务提供了基础服务,业务模块可以引用core来快速搭建服务

---

### Configs

你需要配置一些配置项来快速使用
```yaml
-- 用户账号密码登录密码加解密
core.aes.key
core.aes.iv
```


### Functions

---

#### 1. User

This part provides the ability of saving user's base info. 

---

#### 2. Vip

User is recognized as multi-levels which needs to buy target products. 
Vip product is (supposed to) defined in shop part. 
This part is supposed to check user auth by annotation.

---

#### 3. Login

Login part provides user's login action and logout action. And meanwhile provides multi-ways to login through different third platform such as wechat, (alipay?), firebase ...
With basic double token check (access-token realized by jwt - which is easier, and refresh-token)

---

#### 4. AI

如果需要使用chatGPT,你需要配置URL和api-key.此项为可选项,不影响最基本功能的使用
```yaml
core.openai.url
core.openai.api-key
```

#### 5. OCR服务

如果需要使用OCR服务,目前对接了阿里云的OCR服务,需要配置阿里云相关配置来使用OCR服务.此项为可选项,不影响最基本功能的使用
```yaml
core.ocr.ali.access-id
core.ocr.ali.secret-id
core.ocr.ali.endpoint
```

#### 6. OSS服务

如果需要使用OCR服务,目前对接了七牛的OCR服务,需要配置七牛相关配置来使用OSS服务.此项为可选项,不影响最基本功能的使用
```yaml
core.oss.qiniu.token-expire
core.oss.qiniu.access-id
core.oss.qiniu.secret-id
```

#### 7. 短信服务

如果需要使用短信服务,目前对接了阿里云的短信服务,需要配置阿里云相关配置来使用此服务.此项为可选项,不影响最基本功能的使用
```yaml
core.sms.ali.access-id
core.sms.ali.secret-id
core.sms.ali.endpoint
core.sms.ali.template-id
core.sms.ali.sign
```

#### 8. 微信第三方登录

如果需要使用微信第三方登录(小程序或H5),将需要配置对应的appId和secretId.此项为可选项,不影响最基本功能的使用
```yaml
core.wx.mp.app-id
core.wx.mp.secret-id
core.wx.h5.app-id
core.wx.h5.secret-id
```

#### 9. 多媒体服务器

暂不提供服务

