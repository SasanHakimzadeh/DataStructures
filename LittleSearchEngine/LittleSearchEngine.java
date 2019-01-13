package lse;

import java.io.*;
import java.util.*;


public class LittleSearchEngine {

	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	HashSet<String> noiseWords;


	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}


	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
			throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/

		HashMap <String, Occurrence> hmap = new HashMap<String, Occurrence>();
		Scanner inputFile;
		for(inputFile = new Scanner(new File (docFile)); inputFile.hasNext();) 
		{
			String key = getKeyword(inputFile.next());
			if (key == null)
			{
				continue;
			}
			Occurrence n = hmap.get(key);
			if (n == null) 
			{
				hmap.put(key, new Occurrence(docFile, 1));
			}
			else 
			{
				n.frequency++;
			}
		}

		inputFile.close();
		return hmap;
	}


	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		for (String word: kws.keySet()) 
		{
			ArrayList<Occurrence> occurrences = keywordsIndex.get(word);

			if (occurrences == null)
			{
				occurrences = new ArrayList<Occurrence>();
			}
			occurrences.add(kws.get(word));
			insertLastOccurrence(occurrences);
			keywordsIndex.put(word, occurrences);
		}

	}
	
	 


	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/

		char[] z = word.toLowerCase().toCharArray();
		int i = z.length - 1;
		while(i >= 0) 
		{
			if (z[i] ==  ','|| z[i] == '!' || z[i] ==  '?'|| z[i] ==  ':'|| z[i] == '.'||  z[i] ==  ';' ) 
			{ 

				z[i] = ' ';
			}
			else {
				break;
			}

			i --;
		}

		int j = 0;
		while(j < z.length) 
		{
			if (!Character.isLetter(z[j])) 
			{
				if(z[j] != ' ')
				{
					return null;
				}
			}
			j++;
		}

		word =  new String (z);

		word = word.trim();

		if (word.equals("") || (noiseWords.contains(word))) 
		{
			return null;
		}
		else 
		{
			return word;
		}

	}


	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/

		Occurrence o = occs.remove(occs.size() - 1);
		if (occs.size() == 0) 
		{
			occs.add(o);
			return new ArrayList<Integer>();
		}
		ArrayList<Integer> middle = new ArrayList<Integer>();
		int as = -1;
		for(int low = 0, high = occs.size() - 1, mid = 0; low <= high;) 
		{
			mid = (low+high)/2;
			middle.add(mid);
			if (occs.get(mid).frequency < o.frequency)
			{
				high = mid-1; 
				as = mid;
			}
			else if (occs.get(mid).frequency <= o.frequency)
			{ 
				as = mid;
				break;
			}
			else 
			{ 
				low = mid+1; 
				as = mid+1;
			}
		}

		occs.add(as, o);

		return middle;
	}


	public void makeIndex(String docsFile, String noiseWordsFile) 
			throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) 
		{
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) 
		{
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}


	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/



		kw2 = kw2.toLowerCase();
		kw1 = kw1.toLowerCase();

		ArrayList<Occurrence> x2 = keywordsIndex.get(kw2);
		ArrayList<Occurrence> x1 = keywordsIndex.get(kw1);



		if (x1 == null) {
			if(x2 == null) {
				return null;
			}

		}
		
		ArrayList<String> answer = new ArrayList<String>(5);

		if (x1 == null && x2 != null)
		{
				for (int i = 0; i < 5; i++)
				{
					if (i < x2.size()) 
					{
						answer.add(x2.get(i).document);
					}
				}
		}
		else if (x1 != null && x2 == null)
		{
			for (int i = 0; i < 5; i++) 
			{
				if (i < x1.size())
				{
					answer.add(x1.get(i).document);
				}
			}
		}
		else 
		{
			for (int p1 = 0, p2 = 0; answer.size() != 5;)
			{
				if (p1 < x1.size() && p2 < x2.size())
				{
					if (x1.get(p1).frequency >= x2.get(p2).frequency) 
					{
						if (!answer.contains(x1.get(p1).document)) answer.add(x1.get(p1).document);
						p1++;
					}
					else 
					{
						if (!answer.contains(x2.get(p2).document))answer.add(x2.get(p2).document);
						p2++;
					}
				}
				else if (p1 < x1.size() && p2 >= x2.size())
				{
					if (!answer.contains(x1.get(p1).document))answer.add(x1.get(p1).document);
					p1++;
				}
				else if (p1 >= x1.size() && p2 < x2.size())
				{
					if (!answer.contains(x2.get(p2).document))answer.add(x2.get(p2).document);
					p2++;
				}
				else 
				{
					break;
				}
			}
		}
		return answer;
	}

	
}