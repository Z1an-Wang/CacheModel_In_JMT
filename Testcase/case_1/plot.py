import matplotlib.pyplot as plt
import pandas as pd

# header = ["Distribution", "ttl", "DisParameter 1", "DisParameter 2",
#           "Replacement Policy", "NumberOfItems", "CacheCapacity", "Lower", "Mean", "Upper"]

result = pd.read_csv('result.csv', header=0)

fig = plt.figure(figsize=(10, 10))

z05 = result[result['DisParameter 1']==0.5]
rr = z05[z05['Replacement Policy'] == 'Random']
fifo = z05[z05['Replacement Policy'] == 'FIFO']
lru = z05[z05['Replacement Policy'] == 'LRU']
lfu = z05[z05['Replacement Policy'] == 'LFU']
plt.subplot(2, 2, 1)
plt.title("Replacement Policy - Cache Capacity Zipf(0.5)")
plt.xlabel('Cache Capacity')
plt.ylabel("Cache Hit Rate")
plt.plot(rr['CacheCapacity'], rr['Mean'], label='Random')
plt.plot(fifo['CacheCapacity'], fifo['Mean'], label='FIFO')
plt.plot(lru['CacheCapacity'], lru['Mean'], label='LRU')
plt.plot(lfu['CacheCapacity'], lfu['Mean'], label='LFU')
plt.legend()


z075 = result[result['DisParameter 1']==0.75]
rr = z075[z075['Replacement Policy'] == 'Random']
fifo = z075[z075['Replacement Policy'] == 'FIFO']
lru = z075[z075['Replacement Policy'] == 'LRU']
lfu = z075[z075['Replacement Policy'] == 'LFU']
plt.subplot(2, 2, 2)
plt.title("Replacement Policy - Cache Capacity Zipf(0.75)")
plt.xlabel('Cache Capacity')
plt.ylabel("Cache Hit Rate")
plt.plot(rr['CacheCapacity'], rr['Mean'], label='Random')
plt.plot(fifo['CacheCapacity'], fifo['Mean'], label='FIFO')
plt.plot(lru['CacheCapacity'], lru['Mean'], label='LRU')
plt.plot(lfu['CacheCapacity'], lfu['Mean'], label='LFU')
plt.legend()

z1 = result[result['DisParameter 1'] == 1]
rr = z1[z1['Replacement Policy'] == 'Random']
fifo = z1[z1['Replacement Policy'] == 'FIFO']
lru = z1[z1['Replacement Policy'] == 'LRU']
lfu = z1[z1['Replacement Policy'] == 'LFU']
plt.subplot(2, 2, 3)
plt.title("Replacement Policy - Cache Capacity Zipf(1.0)")
plt.xlabel('Cache Capacity')
plt.ylabel("Cache Hit Rate")
plt.plot(rr['CacheCapacity'], rr['Mean'], label='Random')
plt.plot(fifo['CacheCapacity'], fifo['Mean'], label='FIFO')
plt.plot(lru['CacheCapacity'], lru['Mean'], label='LRU')
plt.plot(lfu['CacheCapacity'], lfu['Mean'], label='LFU')
plt.legend()

z15 = result[result['DisParameter 1'] == 1.5]
rr = z15[z15['Replacement Policy'] == 'Random']
fifo = z15[z15['Replacement Policy'] == 'FIFO']
lru = z15[z15['Replacement Policy'] == 'LRU']
lfu = z15[z15['Replacement Policy'] == 'LFU']
plt.subplot(2, 2, 4)
plt.title("Replacement Policy - Cache Capacity Zipf(1.5)")
plt.xlabel('Cache Capacity')
plt.ylabel("Cache Hit Rate")
plt.plot(rr['CacheCapacity'], rr['Mean'], label='Random')
plt.plot(fifo['CacheCapacity'], fifo['Mean'], label='FIFO')
plt.plot(lru['CacheCapacity'], lru['Mean'], label='LRU')
plt.plot(lfu['CacheCapacity'], lfu['Mean'], label='LFU')
plt.legend()

# plt.show()
plt.savefig("1.png")

# ax.set(xlim=(0, 10), xticks=np.arange(0, 10),
#        ylim=(0, 10), yticks=np.arange(0, 10))
