import matplotlib.pyplot as plt
import pandas as pd

# header = ["Distribution", "ttl", "DisParameter 1", "DisParameter 2",
#           "Replacement Policy", "NumberOfItems", "CacheCapacity", "Lower", "Mean", "Upper"]

result = pd.read_csv('result.csv', header=0)

# fig = plt.figure(figsize=(10, 10))

plt.title("Replacement Policy - Zipf Distribution (Cache Capacity: 400/1000)")
plt.xlabel('Alpha')
plt.ylabel("Cache Hit Rate")

rr = result[result['Replacement Policy'] == 'Random']
fifo = result[result['Replacement Policy'] == 'FIFO']
lru = result[result['Replacement Policy'] == 'LRU']
lfu = result[result['Replacement Policy'] == 'LFU']


plt.plot(rr['DisParameter 1'], rr['Mean'], label='Random')
plt.plot(fifo['DisParameter 1'], fifo['Mean'], label='FIFO')
plt.plot(lru['DisParameter 1'], lru['Mean'], label='LRU')
plt.plot(lfu['DisParameter 1'], lfu['Mean'], label='LFU')

plt.legend()

# plt.show()
plt.savefig("1.png")
