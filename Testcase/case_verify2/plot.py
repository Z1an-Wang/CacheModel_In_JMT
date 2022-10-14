import matplotlib.pyplot as plt
import pandas as pd

# header = ["Distribution", "ttl", "DisParameter 1", "DisParameter 2",
#           "Replacement Policy", "NumberOfItems", "CacheCapacity", "Lower", "Mean", "Upper"]

result = pd.read_csv('result.csv', header=0)

fig = plt.figure(figsize=(10 ,5))


d1 = result[result['DisParameter 1'] == 0.75]
lru = d1[d1['Replacement Policy'] == 'LRU']
lfu = d1[d1['Replacement Policy'] == 'LFU']
plt.subplot(1, 2, 1)

plt.title("LRU/LFU - Cache Capacity (Zipf(0.75))")
plt.xlabel('Cache Capacity')
plt.ylabel("Cache Hit Rate")
plt.plot(lru['CacheCapacity'], lru['Mean'], label='LRU', marker='o', linestyle='-.')
plt.plot(lfu['CacheCapacity'], lfu['Mean'], label='LFU', marker='v', linestyle=':')
plt.legend()

d2 = result[result['DisParameter 1'] == 1.0]
lru = d2[d2['Replacement Policy'] == 'LRU']
lfu = d2[d2['Replacement Policy'] == 'LFU']
plt.subplot(1, 2, 2)

plt.title("LRU/LFU - Cache Capacity (Zipf(1.0))")
plt.xlabel('Cache Capacity')
plt.ylabel("Cache Hit Rate")
plt.plot(lru['CacheCapacity'], lru['Mean'], label='LRU', marker='o', linestyle='-.')
plt.plot(lfu['CacheCapacity'], lfu['Mean'], label='LFU', marker='v', linestyle=':')
plt.legend()




# plt.show()
plt.savefig("1.png")
