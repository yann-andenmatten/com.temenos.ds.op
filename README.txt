Structure
=========

refex/ has "real" but just "example" modeling plug-ins.

base/ has build scripts etc. and raw low level infrastructure code shared between refex and in-house products. 


How to build
============

1. Initially, just once: rm -rf mirror/; mvn -f mirror-pom.xml prepare-package
2. Then just mvn -o clean package! ;-)
3. Tata: $ env UBUNTU_MENUPROXY=0 SWT_GTK3=0 base/releng/com.temenos.ds.op.base.sdk.repository/target/products/com.temenos.ds.op.sdk.ide.product/linux/gtk/x86_64/eclipse
   OR less base/releng/com.temenos.ds.op.base.sdk.repository/target/products/com.temenos.ds.op.sdk.ide.product/linux/gtk/x86_64/configuration/*.log

The idea is to develop using the DS.open SDK Package which has everything needed (at the right version),
and not your own Eclipse download.  That product is built by this project, look around and learn how.

To speed up builds to the max, we normally operate in Maven offline mode;
set Window > Preferences > Maven: Offline, and dito in mvn Launch Configs 
(unless you've changed dependencies and need to reload something). -- We
typically also use the Maven Launcher in Eclipse (M2E), and not the CLI;
see the mvn.launch & mvn-o.launch in the various projects. 
We like option Resolve Workspace Artifacts.

Build the offline aggregate mirror of all needed p2 repos by opening the *.b3aggr, and right-click Build Aggregation.


Various Tips & Tricks
=====================

In each *.feature, remove the url= and use version="0.0.0" instead of version="1.0.0.qualifier" inside <feature ...>.
Remember that in *.feature sometimes (but not always) you have to append .feature in <includes id="...feature"> - does NOT match JAR filename.

In each *.product, in Dependencies, remove the Version of each Feature.

In category.xml <feature> remove fixed version numbers in url and version and use .qualifier.
