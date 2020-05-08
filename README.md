# ProfitBot


A java program to (hopefully) automate investing for automated profit. Input a stock and starting amount of cash and watch the program run an investment simulation!

EDIT: For some reason I had no idea that algo trading was already a huge thing people on main street are already doing. I'm going to research a lot of algo trading stratagies to drastically improve this program. More to come soon! Anyways, here's some info on the existing program in this repo... 

Run the Driver.java file to get started. Driver uses args[] to operate. The necessary arguments are (in order): 1) The symbol of the stock you want to analyze. The stock must be downloaded as a csv from Yahoo Finance's webpage (samples are included). Do not include the ".csv" at the end, only the symbol (Example: Boeing -> "BA" not "BA.csv"). 2) Starting cash. Start with higher amounts for more stable results. 3) Threshold value. If the chaos index exceeds this threshold, a share will be purchased. Chaos indexes are a rating for a stock's buyability and subsequently determine how the program purchases shares. Higher chaos indexes represent a stock more recommended to purchase. The higher the threshold, the more conservative the program will be to purchase. 

This is obviously a work in progress! I plan on changing the chaos index algorithm dramatically as I learn more numeric investing strategies.
