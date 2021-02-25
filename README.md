# MSc-Thesis

Installazione di CORE per la versione 20.04 di Ubuntu:

CORE fornisce uno script per automatizzare l'installazione dell'ambiente virtuale. Il processo di installazione automatico è indicato al seguente [link](https://coreemu.github.io/core/install.html#automated-install). In breve i comandi da eseguire sono:
* git clone https://github.com/coreemu/core.git
*  cd core
* ./install.sh 

Dopo che l'installazione è avvenuta con successo si dovrebbe essere in grado di eseguire core-daemon e core-pygui. Il demone core può essere avviato direttamente da command line, utile per vedere i log generati: **sudo core-daemon --ovs**. Il flag *--ovs* permette di creare tutti gli switch Ethernet istanziati sull'ambiente CORE usando openvswitch, eseguito non nel contesto del singolo nodo. Questo significa che nella macchina host su cui è eseguito CORE è possibile vedere tutte le interfacce che definiscono la rete. Per interconnettere le interfacce dei nodi vengono utilizzate coppie di *veth*. Utilizzando 
