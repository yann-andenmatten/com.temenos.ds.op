Structure
=========

refex/ has "real" but just "example" modeling plug-ins.

base/ has build scripts etc. and raw low level infrastructure code shared between refex and in-house products. 


How to build
============

1. Initially, just once: rm -rf mirror/; mvn -f mirror-pom.xml prepare-package
2. Then just mvn -o clean package! ;-)
3. Tata: $ base/releng/com.temenos.ds.op.base.sdk.repository/target/products/com.temenos.ds.op.sdk.ide.product/linux/gtk/x86_64/eclipse
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

For each *.repository, remove the url= and version="0.0.0" instead of version="1.0.0.qualifier" in side <feature ...>.

For each *.product, in Dependencies, remove the Version of each Feature.

When problems with depending on (external bleeding edge) features,
it's better to have own own feature.xml depend on the external one (Included Feature).

In category.xml <feature> remove fixed version numbers in url and version and use .qualifier.
