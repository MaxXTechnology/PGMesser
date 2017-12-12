#!/bin/bash
# 默认放在工程根目录下
# array里面填写改工程下所有需要发布的模块，注意按依赖顺序写入，比如library1依赖library2，则library2在前，library1在后
# ------配置区域------
array=(
pgmesser-release
)
myPath=./releaseNote/


# ------执行区域------
if [ ! -d "$myPath" ];then
    mkdir "$myPath"
fi

version=$(grep "publish_version=" gradle.properties)
version=${version#*publish_version=}
output="$myPath$version.txt"

count=${#array[@]}
echo '' |tee -a ${output}
echo '------------------------------------------------------------------------------------------------------------------------' |tee -a ${output}
date |tee -a ${output}
echo '------------------------------------------------------------------------------------------------------------------------' |tee -a ${output}
for (( i = 0; i < count; i+=1 )); do
	./gradlew -q ${array[i]}:uploadArchives |tee -a ${output}
	./gradlew -q ${array[i]}:dependencies --configuration archives |tee -a ${output}
done
echo '------------------------------------------------------------------------------------------------------------------------' |tee -a ${output}
echo 'END_OF_PUBLISH' |tee -a ${output}
echo '------------------------------------------------------------------------------------------------------------------------' |tee -a ${output}
echo '' |tee -a ${output}
echo '' |tee -a ${output}

