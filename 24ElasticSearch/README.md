# 1 ES的docker单机版安装
```shell
# 拉取镜像
docker pull elasticsearch:6.8.18
# 创建网络
docker network create es-single-network
# 创建容器，并使用刚才创建的网络
docker run -d --name elasticsearch-single-9200 \
          --net es-single-network \
          -p 9200:9200 -p 9300:9300 \
          -e "discovery.type=single-node" \
          elasticsearch:6.8.18
```
# 2 基本概念
- `索引` 含有相同属性的文档集合
- `类型` 索引可以定义一个或多个类型，文档必须属于一个类型
- `文档` 文档是可以被索引的基本数据单位
- `分片` 每个索引都有多个分片，每个分片是一个Lucene索引
- `备份` 拷贝一份分片就完成了分片的备份

> 分片只能在创建索引时指定，二备份数可以在后期调整

# 3 索引的简单创建
## 3.1 创建结构化索引
- 结构：http://{host}:{port}/{index}
> http://192.168.0.150:9200/people
- Method: PUT
- Body:
```json
{
  "setting": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  },
  "mappings": {
    "man": {
      "properties": {
        "name": {
          "type": "text"
        },
        "country": {
          "type": "keyword"
        },
        "age": {
          "type": "integer"
        },
        "date": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
        }
      }
    }
  }
}
```
响应结果：
```json
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "book"
}
```
类型说明：
- `text` 会被分词的字符串
- `keyword` 不会被分词
- `integer` 数值类型
- `date` 日期类型，根据 `format` 指定日期格式化， `||` 表示可以同时支持多中类型的日期格式

## 3.2 索引创建的结构
- 结构：http://{host}:{port}/{index}/{type}/_mappings
> http://192.168.0.150:9200/book/novel/_mappings
- Method：PUT
- body
```json
{
  "novel": {
    "properties": {
      "title": {
        "type": "text"
      }
    }
  }
}
```
> ES6.0+后，创建索引，不支持传多个type，那么多个type怎么处理？

# 4 文档的简单增删改查
## 4.1 文档的新增，并指定ID
> 也可以直接使用玉按照ID更新
- 格式： http://{host}:{port}/{index}/{type}/{id}
> http://192.168.0.150:9200/people/man/1
- 方法： POST
- Body
```json
{
  "name": "微阳",
  "country": "China",
  "age": 30,
  "date": "2001-08-24"
}
```
请求结果：
```json
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```
## 4.2 文档的新增，不指定ID
- 格式： http://{host}:{port}/{index}/{type}
> http://192.168.0.150:9200/people/man
- 方法： POST
- Body
```json
{
  "name": "没ID的微阳",
  "country": "China",
  "age": 30,
  "date": "2001-08-24"
}
```
- 应答结果：
```json
{
    "_index": "people",
    "_type": "man",
    "_id": "PViFeHsBP-8ITzMiR_RK",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```
> 未指定ID时，ID将自动生成；生产中ID建议使用`数据主键`
## 4.3 文档修改
**修改**
- 直接修改文档
- 脚本修改文档
### 4.3.1 指定ID直接修改文档
> 给新增类似，如果ID存在就更新，不存在就新增
- 格式：http://{host}:{port}/{index}/{type}/{id}
> http://192.168.0.150:9200/people/man/1
- 方法：POST
- Body:
```json
{
  "name": "微阳很厉害",
  "country": "China",
  "age": 30,
  "date": "2001-08-24"
}
```
- 应答结果
```json
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 5,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 4,
    "_primary_term": 1
}
```
### 4.3.2 指定ID基于脚本修改文档
- 格式：http://{host}:{port}/{index}/{type}/{id}/_update
> http://192.168.0.150:9200/people/man/1/_update
- 方法：POST
- Body:
```json
{
  "script": {
    "lang": "painless",
    "inline": "ctx._source.age += 10"
  }
}
```
### 4.3.3 指定ID和参数基于脚本修改文档
- 格式：http://{host}:{port}/{index}/{type}/{id}/_update
> http://192.168.0.150:9200/people/man/1/_update
- 方法：POST
- Body:
```json
{
  "script": {
    "lang": "painless",
    "inline": "ctx._source.age = params.age",
    "params": {
      "age": 100
    }
  }
}
```
- 应答结果
```json
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 12,
    "result": "updated",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 12,
    "_primary_term": 1
}
```
该示例使用`painless`脚本，并且携带参数`params`，通过脚本进行数据变更
## 4.4 删除数据
- 格式：http://{host}:{port}/{index}/{type}/{id}
> http://192.168.0.150:9200/people/man/1
- 方法：DELETE
- Body: none
- 应答结果
```json
{
    "_index": "people",
    "_type": "man",
    "_id": "1",
    "_version": 13,
    "result": "deleted",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 13,
    "_primary_term": 1
}
```
## 4.5 删除索引
- 格式：http://{host}:{port}/{index}
> http://192.168.0.150:9200/people
- 方法：DELETE
- Body: none
- 应答结果
```json
{
    "acknowledged": true
}
```
# 5 数据的复杂查询
mapping准备：
- URL: http://192.168.0.150:9200/book
- METHOD: PUT
- Body： mapping
```json
{
  "setting": {
    "number_of_shards": 3,
    "number_of_replicas": 1
  },
  "mappings": {
    "novel": {
      "properties": {
        "word_count": {
          "type": "integer"
        },
        "author": {
          "type": "keyword"
        },
        "title": {
          "type": "text"
        },
        "publish_date": {
          "type": "date",
          "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
        }
      }
    }
  }
}
```
- 应答结果
```json
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "book"
}
```
准备示例文档：

```text
{"author":"微阳","word_count":7000,"title":"你若不勇敢，谁替你坚强","publish_date":"2001-08-24"}
{"author":"贾平凹","word_count":5000,"title":"自在独行","publish_date":"2001-06-20"}
{"author":"埃.奥.卜劳恩","word_count":3000,"title":"父与子全集","publish_date":"1970-05-14"}
{"author":"卡伦.霍妮","word_count":2000,"title":"我们时代的神经症人格","publish_date":"1989-04-20"}
{"author":"阿尔弗雷德.阿德勒","word_count":1000,"title":"自卑与超越","publish_date":"1989-04-20"}
```
## 5.1 主键查询
- URL: /book/novel/1
- Method: GET
## 5.2 条件查询，查询所有
- URL: /book/_search
- Method: POST
- Body:
```json
{
  "query": {
    "match_all": {}
  },
  "from": 1,
  "size": 2
}
```
说明：
- 查询全部，从第一条开始，总共查2条
- 默认按照score进行排序
## 5.3 根据title查询，并排序
- URL: /book/_search
- Method: POST
- Body:
```json
{
  "query": {
    "match": {
      "title": "自在独行"
    }
  },
  "sort": [
    {
      "publish_date": {
        "order": "desc"
      }
    }
  ]
}
```
说明：
> 自定义排序规则后，score分就会为空

## 5.4 聚合查询
### 5.4.1 根据word_count进行聚合查询
- URL: /book/_search
- Method: POST
- Body:
```json
{
  "aggs": {
    "group_by_word_count": {
      "terms": {
        "field": "word_count"
      }
    }
  }
}
```
### 5.4.2 根据word_count和publish_date进行聚合查询
- URL: /book/_search
- Method: POST
- Body:

```json
{
  "aggs": {
    "group_by_word_count": {
      "terms": {
        "field": "word_count"
      }
    },
    "group_by_publish_date": {
      "terms": {
        "field": "publish_date"
      }
    }
  }
}
```
### 5.4.3 Grades查询！！！！！！！！！！
- URL: /book/_search
- Method: POST
- Body:

```json
{
  "aggs": {
    "grades_word_count": {
      "stats": {
        "field": "word_count"
      }
    }
  }
}
```

# 6 高级查询
- `子条件查询` 特定字段查询所指特定值
- `符合条件查询` 以一定的逻辑组合子条件查询
## 6.1 子条件查询
- Query context
- Filter context

QueryContext
> 在查询过程中，除了判断文档是否满足查询条件外，
> ES还会计算一个`_score`来标识匹配的程度，
> 旨在判断目标文档和查询条件匹配的`有多好` (`吻合度`)

常用查询：
- `全文本查询` 针对文本类型数据
- `字段级别的查询` 针对结构化数据，如数字、日期等
### 6.1.1 match模糊匹配查询

```json
{
  "query": {
    "match": {
      "author": "贾平凹"
    }
  }
}
```

### 6.1.2 match_phrase精确查询

```json
{
  "query": {
    "match_phrase": {
      "title": "你若不勇敢，谁替你坚强"
    }
  }
}
```

### 6.1.3 multi_match多个字段模糊匹配

```json
{
  "query": {
    "multi_match": {
      "query": "贾平凹",
      "fields": ["author", "title"]
    }
  }
}
```
> 使用`fields`指定多个字段进行匹配查询

### 6.1.4 query_string语法查询 `AND` `OR`

```json
{
  "query": {
    "query_string": {
      "query": "(关键字1 AND 关键字2) OR 关键字3"
    }
  }
}
```

```json
{
  "query": {
    "query_string": {
      "query": "关键字1 OR 关键字2",
      "fields": ["title", "author"]
    }
  }
}
```
### 6.1.5 `term`指定字段进行查询
> 具体性查询： `term`
```json
{
  "query": {
    "term": {
      "word_count": 1000
    }
  }
}
```
范围查询：

```json
{
  "query": {
    "range": {
      "word_count": {
        "gte": 1000,
        "lte": 3000
      }
    }
  }
}
```
> `range` 针对数值范围进行查询，也可以针对日期进行范围查询

```json
{
  "query": {
    "range": {
      "publish_date": {
        "gt": "2021-08-27",
        "lte": "now"
      }
    }
  }
}
```
> 日期值表示上，可以使用关键字表示日期，比如： `now`

## 6.2 子条件查询，Filter Context
在查询过程中，只判断该文档是否**满足**条件，只有Yes/No的概念

```json
{
  "query": {
    "bool": {
      "filter": {
        "term": {
          "word_count": 1000
        }
      }
    }
  }
}
```
> `filter`关键字必须与`bool`一起使用
# 7 符合条件查询
常用查询
- 固定分数查询
- bool查询
- ...more

```json
{
  "query": {
    "match": {
      "title": "关键字"
    }
  }
}
```
## 7.1 固定分数查询
```json
{
  "query": {
    "constant_score": {
      "filter": {
        "match": {
          "title": "关键字"
        }
      }
    }
  }
}
```
固定分支查询，查询的分数都是1
## 7.2 固定分值查询，并指定分数

```json
{
  "query": {
    "constant_score": {
      "filter": {
        "match": {
          "title": "关键字"
        }
      },
      "boost": 2
    }
  }
}
```
- 固定分值查询时，还可以通过关键字`boost`指定分数
- 固定分值查询时，不支持`match`查询，只支持`filter`查询
## 7.3 `should`查询，或的关系查询
```json
{
  "query": {
    "bool": {
      "should": [
        {
          "match": {
            "author": "关键字1"
          }
        },
        {
          "match": {
            "title": "关键字2"
          }
        }
      ]
    }
  }
}
```
## 7.4 `must`且的关系

```json
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "author": "关键字1"
          }
        },
        {
          "match": {
            "title": "关键字2"
          }
        }
      ]
    }
  }
}
```
## 7.5 `filter`与`must`结合使用

```json
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "author": "关键字1"
          }
        },
        {
          "match": {
            "title": "关键字2"
          }
        }
      ],
      "filter": [
        {
          "term": {
            "word_count": 1000
          }
        }
      ]
    }
  }
}
```
## 7.6 `must_not`非

```json
{
  "query": {
    "bool": {
      "must_not": {
        "term": {
          "author": "关键字"
        }
      }
    }
  }
}
```
# 8 Java客户端说明
## 8.1 ESJavaClient的历史
### 8.1.1 JavaAPI Client
- 优势：基于transport进行数据访问，能够使用ES集群内部的性能特性，性能相对好
- 劣势：client版本需要和es集群版本一致，数据序列化通过java实现，es集群或jdk升级，客户端需要伴随升级。

ES官网最早提供的Client，spring-data-elasticsearch也基于该client开发，
使用transport接口进行通信，其工作方式是将webserver当做集群中的节点，
获取集群数据节点(DataNode)并将请求路由到Node获取数据将，
返回结果在webserver内部进行结果汇总。 
client需要与es集群保持相对一致性，否则会出现各种『奇怪』的异常。
由于ES集群升级很快，集群升级时客户端伴随升级的成本高。

官网已声明es7.0将不在支持transport client(API Client),8.0正时移除

### 8.1.2 REST Client
- 优势：REST风格交互，符合ES设计初衷；兼容性强；
- 劣势：性能相对API较低

ESREST基于http协议，客户端发送请求到es的任何节点，节点会通过transport接口将请求路由到其他节点，完成数据处理后汇总并返回客户端，客户端负载低。

RestFul是ES特性之一，但是直到5.0才有了自己的客户端，6.0才有的相对好用的客户端。在此之前JestClient作为第三方客户端，使用非常广泛。

## 8.2 客户端的分类
- Transport
- Java Rest Client
- Java High Level Client
- Jest
- Java Lower Level Client

## 8.3 参考文档：
[Elasticsearch Java Rest Client简述](https://www.cnblogs.com/549294286/p/10284343.html)
[官方Rest客户端文档](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.8/index.html)


