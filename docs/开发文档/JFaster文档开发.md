# 文档开发

## 环境准备

### Python

需要 Python 和 Python package manager pip 来安装 MkDocs . 可以通过以下命令查看是否安装了上述依赖:

```
$ python --version
Python 2.7.2
$ pip --version
pip 1.5.2
```
MkDocs 支持 Python 2.6, 2.7, 3.3 和 3.4.

### mkdocs

使用 pip 安装 mkdocs :

```
$ pip install -i https://mirrors.aliyun.com/pypi/simple/ mkdocs
```

### 安装mkdocs主题

```
pip install  -i https://pypi.tuna.tsinghua.edu.cn/simple mkdocs-bootswatch
```

## 说明

### 目录结构

```
mkdocs.yml
docs/
    index.md
```

### 本地调试

```
$ mkdocs serve
Running at: http://127.0.0.1:8000/
```

### 编译html

```
$ mkdocs serve
Running at: http://127.0.0.1:8000/
```

> 参考

- 中文手册：[https://markdown-docs-zh.readthedocs.io/zh_CN/latest/](https://markdown-docs-zh.readthedocs.io/zh_CN/latest/)
- bootswatch：[https://mkdocs.github.io/mkdocs-bootswatch/](https://mkdocs.github.io/mkdocs-bootswatch/)