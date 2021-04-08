# 使用步骤
比较完整的实例： https://github.com/corningsun/yuchigong/blob/httpClient/httpClient/README.md
## 1. 在工程项目的根目录中添加配置文件 `http-client.env.json`
```json
{
  "local": {
    "host": "localhost:9200"
  },
  "prd": {
    "host": "a.b.com:9200"
  }
}
```
可以配置多套环境的配置， 比如 `local` `prd` 代表两套不同的环境，然后就可以再不同的环境中定义不同的配置了

## 2. 