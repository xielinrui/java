使用方法：
1、在云服务器中搭建docker环境，并利用docker搭建hadoop集群。
2、利用moven分别根据Phone.java和logcount.java来构建对应的jar包，加上filelog.txt和filephone.txt,qingchuhuanjing.sh,dosomething.sh,runPhone.sh,run-logcount.sh上传到你的服务器中。
3、进入到服务器后，打开hadoop集群环境后，使用docker ps查看hadoop-master的容器ID，使用docker exec -it 容器ID /bin/bash 进入到hadoop-master中，然后在当前目录新建inputlog和inputPhone两个目录。
4、回到服务器shell界面，使用 bash ./dosomething.sh 把所有东西复制到hadoop集群中。然后进入到hadoop-master界面，使用 bash ./qingchuhuanjing.sh 运行mapreducer算法。
