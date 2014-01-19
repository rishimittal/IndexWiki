IndexWiki
=========

IndexWiki is the scalable IRE system capable of indexing 100Mb of data in 
about 14-15 secs ,this is complete project with minimum machine requirement.
It internally performs the external merging , It almost creates 100-200 sorted
files depending upon the size of the of the Corpus. Here ideally it has the 
corpus size of around 100-150 MB . It works fine.


Key Points : 
	
1. Uses External merging for the merging the sorted files
2. In searching module uses the .01 secs to search in the index of
   17 Mb. It also depends on the system configuration.(Uses basic
   binary search in files)
3. Currently it does not categorize in the different fields but it
   realises all the keywords and index them accordingly.
4. Uses standard porter stemming algorithm
5. Basic StopWord List , which is set up there in the StopWordHandle 
   class.


Please comment if you found any issue with the application.

