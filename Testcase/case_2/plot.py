import matplotlib.pyplot as plt
import pandas as pd

# header = ["Distribution", "ttl", "DisParameter 1", "DisParameter 2",
#           "Replacement Policy", "NumberOfItems", "CacheCapacity", "Lower", "Mean", "Upper"]

result = pd.read_csv('result.csv', header=0)

# fig = plt.figure(figsize=(10, 10))

plt.title("TTL - Cache Capacity (Zipf(1.0))")
plt.xlabel('Cache Capacity')
plt.ylabel("Cache Hit Rate")

for i in range(55,705, 50):
    temp = result[result['ttl']==i]
    plt.plot(temp['CacheCapacity'], temp['Mean'], label='TTL-'+str(i))

lru = result[result['Replacement Policy'] == 'LRU']
plt.plot(lru['CacheCapacity'], lru['Mean'], label='LRU')
plt.legend()


# plt.show()
plt.savefig("1.png")
