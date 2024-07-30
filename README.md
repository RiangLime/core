# core

---

### Intro
provide base infrastructure with limited abilities.
Biz modules can use this core module in pom and get started easily with proper configs.

---

### Configs

To get started, u need to set configurations below properly in yaml which in biz module. 
All params defined in CoreParams.java
```yaml
base.snowflake.
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

#### 4. Shop

This part provides the ability of paying. Admins can define products by themselves and users can pay through third platform like wechat, alipay, firebase ...
But need to pay attention to if configs are defined properly.

---
