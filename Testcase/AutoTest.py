import csv
import os
from xml.dom import minidom as dom

SEED = 4546
MAXTIME = 120

INITIAL_SOURCE_DIS = 2.0
INITIAL_CACHE_CAPACITY = 1
INITIAL_CACHE_ITEM = 10

DIS_PARA_1 = 1.0
DIS_PARA_2 = 10

JSIM_ROOT = '/Users/prince_an/JMT/examples/template/'
XML_FILE = 'template.jsimg'
# XML_FILE = 'template_withQ.jsimg'
CSV_FILE = './result.csv'
JAR_ROOT = '/Users/prince_an/Desktop/IC/Final/Code/JMT_V1.1/target/JMT-singlejar-1.2.1.jar'

DEFAULT_STRATEGY = FIFO = 'FIFO'
RR = "Random"
LRU = "LRU"
LFU = "LFU"
TTL = "TTL"
RANDOM_CACHE_STRATEGY = 'jmt.engine.NetStrategies.CacheStrategies.RandomCache'
FIFO_CACHE_STRATEGY = 'jmt.engine.NetStrategies.CacheStrategies.FIFOCache'
LRU_CACHE_STRATEGY = 'jmt.engine.NetStrategies.CacheStrategies.LRUCache'
LFU_CACHE_STRATEGY = 'jmt.engine.NetStrategies.CacheStrategies.LFUCache'
TTL_CACHE_STRATEGY = 'jmt.engine.NetStrategies.CacheStrategies.TTLCache'

DEFAULT_POP = ZIPF = "Zipf"
UNIFORM = "Uniform"
BINOMIAL = "Binomial"
ZIPF_DIS = 'jmt.engine.random.discrete.Zipf'
UNIFORM_DIS = 'jmt.engine.random.discrete.Uniform'
BINOMIAL_DIS = 'jmt.engine.random.discrete.Binomial'

initial_DomTree = dom.parse(JSIM_ROOT+XML_FILE)


def changeSourceDis(lambda_, tree=initial_DomTree):

    root = tree.documentElement

    nodes = root.getElementsByTagName('node')
    source_node = nodes[1]
    if(source_node.getAttribute("name") != "Source 1"):
        print("Wrong RandomSource node")
        return

    RandomSource = source_node.getElementsByTagName('section')[0]
    if(RandomSource.getAttribute("className") != "RandomSource"):
        print("Wrong RandomSource section")
        return

    lambda_value = RandomSource.getElementsByTagName('parameter')[0]\
        .getElementsByTagName('subParameter')[0]\
            .getElementsByTagName('subParameter')[1]\
                .getElementsByTagName('subParameter')[0]\
                    .getElementsByTagName('value')[0]

    lambda_value.firstChild.data = lambda_

    return tree


def changeNumOfItems_Capacity(numberOfItems, capacity, tree=initial_DomTree):

    root = tree.documentElement

    nodes = root.getElementsByTagName('node')
    cache_node = nodes[2]
    if(cache_node.getAttribute("name") != "Cache 1"):
        print("Wrong cache node")
        return

    cache_section = cache_node.getElementsByTagName('section')[1]
    if(cache_section.getAttribute("className") != "Cache"):
        print("Wrong cache section")
        return

    itemNum_value = cache_section.getElementsByTagName(
        'parameter')[0].getElementsByTagName('value')[0]

    itemNum_value.firstChild.data = numberOfItems

    capacity_value = cache_section.getElementsByTagName(
        'parameter')[1].getElementsByTagName('value')[0]

    capacity_value.firstChild.data = capacity

    return tree


def changeCacheStrategy(name, ttl_name = '', ttl_value = 0, tree=initial_DomTree):
    root = tree.documentElement

    nodes = root.getElementsByTagName('node')
    cache_node = nodes[2]
    if(cache_node.getAttribute("name") != "Cache 1"):
        print("Wrong cache node")
        return

    cache_section = cache_node.getElementsByTagName('section')[1]
    if(cache_section.getAttribute("className") != "Cache"):
        print("Wrong cache section")
        return

    cache_strategy = cache_section.getElementsByTagName(
        'parameter')[2]

    cache_strategy.setAttribute('classPath', name)

    if (name == TTL_CACHE_STRATEGY):
        if (cache_section.getElementsByTagName("subParameter")[0].getAttribute('name') == 'TTL'):
            cache_section.getElementsByTagName("subParameter")[
                0].getElementsByTagName('value')[0].firstChild.data = ttl_value

            cache_section.getElementsByTagName("subParameter")[
                1].setAttribute('classPath', ttl_name)
        else:
            subPar1 = tree.createElement('subParameter')
            subPar1.setAttribute('classPath', 'java.lang.Double')
            subPar1.setAttribute('name', 'TTL')

            value = tree.createElement('value')
            text = tree.createTextNode(str(ttl_value))
            value.appendChild(text)
            subPar1.appendChild(value)
            cache_strategy.appendChild(subPar1)

            subPar2 = tree.createElement('subParameter')
            subPar2.setAttribute('classPath', ttl_name)
            subPar2.setAttribute('name', 'TTL')
            cache_strategy.appendChild(subPar2)

    return tree


def changePopularity(name, a, b, tree=initial_DomTree):
    root = tree.documentElement

    nodes = root.getElementsByTagName('node')
    cache_node = nodes[2]
    if(cache_node.getAttribute("name") != "Cache 1"):
        print("Wrong cache node")
        return

    cache_section = cache_node.getElementsByTagName('section')[1]
    if(cache_section.getAttribute("className") != "Cache"):
        print("Wrong cache section")
        return

    popularity_Distribution = cache_section.getElementsByTagName(
        'parameter')[3].getElementsByTagName('subParameter')[0]


    popularity_Distribution.setAttribute('classPath', name)

    if name in [ZIPF_DIS, BINOMIAL_DIS]:
        popularity_Distribution.getElementsByTagName(
            'subParameter')[0].setAttribute('classPath', 'java.lang.Double')
        popularity_Distribution.getElementsByTagName(
            'subParameter')[1].setAttribute('classPath', 'java.lang.Integer')

    if name in [UNIFORM_DIS]:
        popularity_Distribution.getElementsByTagName(
            'subParameter')[0].setAttribute('classPath', 'java.lang.Integer')
        popularity_Distribution.getElementsByTagName(
            'subParameter')[1].setAttribute('classPath', 'java.lang.Integer')


    popularity_Distribution.getElementsByTagName('subParameter')[0]\
        .getElementsByTagName('value')[0].firstChild.data = a

    popularity_Distribution.getElementsByTagName('subParameter')[1]\
        .getElementsByTagName('value')[0].firstChild.data = b

    return tree


def writeExecute(tree, strategy=DEFAULT_STRATEGY, pop=DEFAULT_POP, a=DIS_PARA_1, b=DIS_PARA_2, numOfItems=INITIAL_CACHE_ITEM, cacheCapacity=INITIAL_CACHE_CAPACITY, ttl=0):

    filename = JSIM_ROOT+'test_'+strategy+'_'+pop+'-' + \
        str(a)+'-'+str(b)+'_'+str(numOfItems)+'_'+str(cacheCapacity)+'.jsimg'
    resultFile = JSIM_ROOT+'test_'+strategy+'_'+pop+'-' + \
        str(a)+'-'+str(b)+'_'+str(numOfItems)+'_'+str(cacheCapacity)+'.jsimg-result.jsim'

    if(ttl!=0):
        filename = JSIM_ROOT+'test_'+strategy+str(ttl)+'_'+pop+'-' + \
            str(a)+'-'+str(b)+'_'+str(numOfItems) + \
            '_'+str(cacheCapacity)+'.jsimg'
        resultFile = JSIM_ROOT+'test_'+strategy+str(ttl)+'_'+pop+'-' + \
            str(a)+'-'+str(b)+'_'+str(numOfItems)+'_' + \
            str(cacheCapacity)+'.jsimg-result.jsim'

    with open(filename, "w") as f:
        tree.writexml(f, indent='', addindent='\t',
                    newl='', encoding='UTF-8')

    os.system('java -cp '+JAR_ROOT+' jmt.commandline.Jmt sim '+ filename +' -seed '+str(SEED)+' -maxtime '+str(MAXTIME))

    domTree = dom.parse(resultFile)
    resultRoot = domTree.documentElement
    measure = resultRoot.getElementsByTagName('measure')[0]
    lower = 0
    upper = 0
    success = measure.getAttribute('successful')
    mean = measure.getAttribute('meanValue')
    if (success == 'true'):
        lower = measure.getAttribute('lowerLimit')
        upper = measure.getAttribute('upperLimit')

    data = [pop, '',a, b, strategy, numOfItems, cacheCapacity, lower, mean, upper]
    if(ttl!=0):
        data = [pop, ttl, a, b, strategy, numOfItems, cacheCapacity, lower, mean, upper]

    with open(CSV_FILE, 'a', encoding='utf-8', newline='') as csvFile:
        writer = csv.writer(csvFile)
        writer.writerow(data)


with open(CSV_FILE, 'w', encoding='utf-8', newline='') as csvFile:
    writer = csv.writer(csvFile)
    writer.writerow(["Distribution", "ttl", "DisParameter 1", "DisParameter 2",
                    "Replacement Policy", "NumberOfItems", "CacheCapacity", "Lower", "Mean", "Upper"])


## case 1
# for alpha in (0.5, 0.75, 1, 1.5, 2):
#     tree0 = changePopularity(ZIPF_DIS, alpha, 1000, changeSourceDis(2.0))

#     for (strategy, name) in [(RANDOM_CACHE_STRATEGY, RR), (FIFO_CACHE_STRATEGY, FIFO), (LRU_CACHE_STRATEGY, LRU), (LFU_CACHE_STRATEGY, LFU)]:
#         tree1 = changeCacheStrategy(strategy, tree0)

#         for Capacity in range(100, 1000, 100):
#             numOfItems = 1000
#             tree = changeNumOfItems_Capacity(numOfItems, Capacity, tree1)

#             writeExecute(tree, strategy=name, pop=ZIPF, a=alpha, b=1000,
#                             numOfItems=numOfItems, cacheCapacity=Capacity)


## case 2
# for ttl_value in range(5, 705, 50):
#     tree0 = changePopularity(ZIPF_DIS, 1.0, 1000, changeSourceDis(2.0))
#     tree1 = changeCacheStrategy(
#         TTL_CACHE_STRATEGY, ttl_name=LRU_CACHE_STRATEGY, ttl_value=ttl_value, tree=tree0)

#     for Capacity in range(100, 500, 50):
#         numOfItems = 1000
#         tree = changeNumOfItems_Capacity(numOfItems, Capacity, tree1)

#         writeExecute(tree, strategy=TTL, pop=ZIPF, a=1.0, b=1000,
#                      numOfItems=numOfItems, cacheCapacity=Capacity, ttl=ttl_value)

## case 3
for (strategy, name) in [(RANDOM_CACHE_STRATEGY, RR), (FIFO_CACHE_STRATEGY, FIFO), (LRU_CACHE_STRATEGY, LRU), (LFU_CACHE_STRATEGY, LFU)]:
    tree1 = changeCacheStrategy(strategy, changeSourceDis(2.0))

    numOfItems = 1000
    tree2 = changeNumOfItems_Capacity(numOfItems, 400, tree1)


    for alpha in range(2, 22, 2):
        tree = changePopularity(ZIPF_DIS, alpha/10, 1000, tree2)
        writeExecute(tree, strategy=name, pop=ZIPF, a=alpha/10, b=1000,
                            numOfItems=numOfItems, cacheCapacity=400)
