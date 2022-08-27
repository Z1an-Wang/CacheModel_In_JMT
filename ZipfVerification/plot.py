import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

AR05 = pd.read_csv('AR_0.5.csv', header=None)
ITS05 = pd.read_csv('ITS_0.5.csv', header=None)

AR1 = pd.read_csv('AR_1.csv', header=None)
ITS1 = pd.read_csv('ITS_1.csv', header=None)

AR15 = pd.read_csv('AR_1.5.csv', header=None)
ITS15 = pd.read_csv('ITS_1.5.csv', header=None)

AR3 = pd.read_csv('AR_3.csv', header=None)
ITS3 = pd.read_csv('ITS_3.csv', header=None)


fig = plt.figure(figsize=(10, 10))

plt.subplot(2, 2, 1)
plt.title("Zipf Distrbution - Aplha 0.5")
plt.xlabel('Item ID')
plt.ylabel("Number of Occurance")
plt.plot(AR05[0], AR05[1], label='Accept-Reject')
plt.plot(ITS05[0], ITS05[1], label='Inverse Transform')
plt.legend()


plt.subplot(2, 2, 2)
plt.title("Zipf Distrbution - Aplha 1.0")
plt.xlabel('Item ID')
plt.ylabel("Number of Occurance")
plt.plot(AR1[0], AR1[1], label='Accept-Reject')
plt.plot(ITS1[0], ITS1[1], label='Inverse Transform')
plt.legend()


plt.subplot(2, 2, 3)
plt.title("Zipf Distrbution - Aplha 1.5")
plt.xlabel('Item ID')
plt.ylabel("Number of Occurance")
plt.plot(AR15[0], AR15[1], label='Accept-Reject')
plt.plot(ITS15[0], ITS15[1], label='Inverse Transform')
plt.legend()

plt.subplot(2, 2, 4)
plt.title("Zipf Distrbution- Aplha 3")
plt.xlabel('Item ID')
plt.ylabel("Number of Occurance")
plt.plot(AR3[0], AR3[1], label='Accept-Reject')
plt.plot(ITS3[0], ITS3[1], label='Inverse Transform')

plt.legend()
plt.savefig("1.png")


# ax.set(xlim=(0, 10), xticks=np.arange(0, 10),
#        ylim=(0, 10), yticks=np.arange(0, 10))
