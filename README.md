# Input method
 
## 一、 实现的功能：

- 拼音的切分
- HMM模型建立
- Viterbi算法。

## 二、 Function building

### 1. HMM模型的建立

> 先来普及一下隐马尔可夫模型（HMM）可以用五个元素来描述，包括2个状态集合和3个概率矩阵：

1）隐含状态 S

- 这些状态之间满足马尔可夫性质，是马尔可夫模型中实际所隐含的状态。这些状态通常无法通过直接观测而得到。（例如S1、S2、S3等等)

2）可观测状态 O

- 在模型中与隐含状态相关联，可通过直接观测而得到。(例如O1、O2、O3等等，可观测状态的数目不一定要和隐含状态的数目一致。）

3）初始状态概率矩阵 π

- 表示隐含状态在初始时刻t=1的概率矩阵，(例如t=1时，P(S1)=p1、P(S2)=P2、P(S3)=p3，则初始状态概率矩阵 π=[ p1 p2 p3 ].

4）隐含状态转移概率矩阵 A。

- 描述了HMM模型中各个状态之间的转移概率。其中Aij = P( Sj | Si ),1≤i,,j≤N. 表示在 t 时刻、状态为 Si 的条件下，在 t+1 时刻状态是 Sj 的概率。

5）观测状态转移概率矩阵 B （英文名为Confusion Matrix，直译为混淆矩阵不太易于从字面理解）。

- 令N代表隐含状态数目，M代表可观测状态数目，则：Bij = P( Oi | Sj ), 1≤i≤M,1≤j≤N； 表示在 t 时刻、隐含状态是 Sj 条件下，观察状态为 Oi 的概率。

**总结**：
一般的，可以用λ=(A,B,π)三元组来简洁的表示一个隐马尔可夫模型。隐马尔可夫模型实际上是标准马尔可夫模型的扩展，添加了可观测状态集合和这些状态与隐含状态之间的概率关系。

在拼音输入法中，O为观测序列即输入的拼音序列，S:需要求解的隐含序列即拼音序列对应的汉字序列
HMM中的三个参数是通过sources文件夹下的文件训练而来，具体参考GenerateBase.java和GenerateFinally.java

### 2. 拼音切分的实现，是利用Trie树实现

1）Trie树，又称单词查找树、字典树，是一种树形结构，是一种哈希树的变种，是一种用于快速检索的多叉树结构。典型应用是用于统计和排序大量的字符串（但不仅限于字符串），所以经常被搜索引擎系统用于文本词频统计。它的优点是：最大限度地减少无谓的字符串比较，查询效率比哈希表高。
   - Trie的核心思想是空间换时间。利用字符串的公共前缀来降低查询时间的开销以达到提高效率的目的。
   - Trie树也有它的缺点,Trie树的内存消耗非常大.当然,或许用左儿子右兄弟的方法建树的话,可能会好点.
   
2）三个基本特性：　　

- 根节点不包含字符，除根节点外每一个节点都只包含一个字符。　

- 从根节点到某一节点，路径上经过的字符连接起来，为该节点对应的字符串。

- 每个节点的所有子节点包含的字符都不相同。

3）TrieNode节点包含：

	- `String name;` //结点的字符名称
	- `int fre;` //单词出现的频率
	- `boolean end;` //是否是单词结尾
	- `boolean root;` //是否是根结点
	- `Map<String, TrieNode> childrens;`//子节点信息
	
**遇到的问题**：

例如：xian可以拆分为xi an（西安） 和xian（先），解决方案，按最长字符串匹配,缺陷是无法完成用户想匹配的单词，可以利用最长和最短字符串同时执行

Viterbi算法：用来求解隐含序列的最大可能性匹配，每次取概率最大的那个。

# 三、运行

##### 1. 首先运行train文件夹下
```
javac ProcessArticle.java      
java  ProcessArticle  
```
会产生一个`sentences.txt`

##### 2. 运行train文件夹下得：
```
javac ProcessHanziPinyin.java  
java ProcessHanziPinyin
```
会产生一个文件`pinyin2hanzi.txt`

##### 3. 生成基础HMM模型

利用`sentence.txt word.txt hanzipinyin.txt`生成HMM模型的三个参数模型`base_start、base_emission、base_transition`
```
javac GenerateBase.java   
java GenerateBase
```
训练产生的数据是出现的次数，需要转换成概率

##### 4. HMM模型finally
```
javac GenerateFinally.java  
java GenerateFinally
```

##### 5. 最终测试
```
javac Test.java  
java Test
```
# note:
当然也可利用eclipse进行编译
	
