#Some static settings
$imageDirIn = "C:\Google Drive\SWGOH\MEmu Download\Screenshot\"
$apiKey = "0f42b4b0791940b5bc1c4b512e2d343b"
#$fileCleanUp = $true #$false #$true


$DebugPreference = "Continue"
#$DebugPreference = "SilentlyContinue"

#This global variable holds the member / lifetime tickets array data
$dataList = New-Object System.Collections.ArrayList


<# ********************************************************************************************************************** #>
Function Get-ImageText{
    [CmdletBinding()]
    Param(
        [Parameter(ValueFromPipeline=$True)]
        [String] $Path
    )

    Process{

        $Uri = 'https://westus.api.cognitive.microsoft.com/vision/v1.0/ocr?language=en&detectOrientation =false'
        $Headers =  @{
            # Your Secret Subscription Key goes here.
            'Ocp-Apim-Subscription-Key' = $apiKey
            'ContentType' = 'application/octet-stream'
        }

        $result = Invoke-RestMethod -Method Post -Uri $Uri -Header $Headers -InFile $Path -ErrorAction Stop -ContentType 'application/octet-stream'
        
        return $result
    }
}

function Get-CurrentLineNumber { 
    $MyInvocation.ScriptLineNumber 
}

function moveFiles ($source, $destination)
{
    $srch = $source + "*.png"
    $mfiles = Get-ChildItem $srch
    for ($i=0; $i -lt $mfiles.Count; $i++) {
       $fileSource = $mfiles[$i].DirectoryName + "\" + $mfiles[$i].BaseName  + $mfiles[$i].Extension
       #Write-Host($fileSource)
       $fileDest = $destination + "\"
       #write-host($fileDest)
       Move-Item $fileSource $fileDest
    }
}

function createCroppedImages ($destFolder) {
    $image = New-Object -ComObject Wia.ImageFile
    $IP = New-Object -ComObject Wia.ImageProcess
    
    $srch = $newDir + "\*.png"
    $files = Get-ChildItem $srch

    for ($i=0; $i -lt $files.Count; $i++) {
        $fileIn = $files[$i].FullName
        #$fileIn = $file.DirectoryName + "\" + $file.BaseName  + $file.Extension
        #Write-Host("function:" + $fileIn)
        $image.LoadFile($fileIn)
        $IP.Filters.Add($IP.FilterInfos("Crop").FilterID)
        #678	388	59	155
        $IP.Filters.Item(1).Properties.Item("Left")   = 678
        $IP.Filters.Item(1).Properties.Item("Top")    = 388
        $IP.Filters.Item(1).Properties.Item("Right")  = 59
        $IP.Filters.Item(1).Properties.Item("Bottom") = 155
        $image = $IP.Apply($image)
        $saveFile = $destFolder + "\crop_" + $files[$i].BaseName + $files[$i].Extension
        $image.SaveFile($saveFile)
    }
}

function loadArrayList ([string]$playerID, [string]$tickets){
    $temp = New-Object System.Object
    $temp | Add-Member -MemberType NoteProperty -Name "playerID" -Value $playerID
    $temp | Add-Member -MemberType NoteProperty -Name "ltTickets" -Value $tickets
    $dataList.Add($temp) | Out-Null
}


function findInArray (){
    Param(
        [string[]]$searchArray,
        [int]$startPosition = 1,
        [string]$searchString
    )
    [int]$returnInt = 0
    for ([int]$i=$startPosition; $i -lt $searchArray.Count; $i++){
        if ( $searchArray[$i] -eq $searchString ) {
            $returnInt = $i
            break
        }
    }
    return $returnInt
}


#This main function is called for each file found.  It assembles and array with the returned text from Microsoft Cognitive services.
function getTicketData(){
    Param(
        [Parameter(ValueFromPipeline=$True)]
        [String] $Path
    )
    #these two variables are just for storing the OCR results in a file with the same name as the input graphics file
    $inputFile = Get-Item -Path $Path
    $textFileOut =  $inputFile.DirectoryName + "\" + $inputFile.BaseName  + ".txt"
    Write-Debug "$(Get-CurrentLineNumber) Output file: $textFileOut"
    #Call the Get-ImageText function and get an array back
    $results = Get-ImageText -Path $Path

    #Here we take the results array and start to reshape it into a more usable format
    $i=0
    [string[]] $resultsArray = @{}
    foreach($D in $results.regions.lines){
        $s="";
        foreach ($word in $D.words){
            $s=$s + $word.text
        }
        $lineNumber = $i
        #Write-Debug $lineNumber ":" $s
        $resultsArray += $s
        $i=$i+1
    }
    #Print the results out to the console and out to a file.
    for ($i=1; $i -lt $resultsArray.Count; $i++) {
        [string]$outLine = [string]$i + "`t" + $resultsArray[$i] #+ "`t" + $(Get-Date -Format yyyy-MM-dd)
        Write-Host $outLine
        #TODO: Write the individual responses out to a file
        Add-Content $textFileOut $outLine
    }
    
    #We have the Array with Data now, we we need to parse it and then load data into the dataList object
    #first see if we have 30 or 29 items in the array
    
    <#  #This old method of figuring out the Member Name and tickets locations was not reliable,  improved code is below
    if ($resultsArray.Count -eq 31){
        #Start loading
        loadArrayList $resultsArray[1] $resultsArray[18]
        loadArrayList $resultsArray[4] $resultsArray[21]
        loadArrayList $resultsArray[7] $resultsArray[24]
        loadArrayList $resultsArray[10] $resultsArray[27]
        loadArrayList $resultsArray[13] $resultsArray[30]

    } else {
        #So this means only 29 rows of data exist because of G
        loadArrayList $resultsArray[1] $resultsArray[17]
        loadArrayList $resultsArray[4] $resultsArray[20]
        loadArrayList "G"              $resultsArray[23]
        loadArrayList $resultsArray[9] $resultsArray[26]
        loadArrayList $resultsArray[12] $resultsArray[29]
    }
    #>
    <#
    # TODO: (Complete) This section really needs to be improved.
    #  Scan through the results and find "85".  The Prior entry will be the name
    #    (If the phrase "Member" is the result, this is probably "G" or we can't find the name
    #  Then Scan through the results and find "TicketsProduced:". The line after this will be the lifetime tickets unless we are at end of file
    #>

    [int]$name1Location = findInArray -searchArray $resultsArray -startPosition 1 -searchString "85" -returnOffset
    [int]$name2Location = findInArray -searchArray $resultsArray -startPosition $($name1Location + 1) -searchString "85"
    [int]$name3Location = findInArray -searchArray $resultsArray -startPosition $($name2Location + 1) -searchString "85"
    [int]$name4Location = findInArray -searchArray $resultsArray -startPosition $($name3Location + 1) -searchString "85"
    [int]$name5Location = findInArray -searchArray $resultsArray -startPosition $($name4Location + 1) -searchString "85"

    [int]$tickets1Location = findInArray -searchArray $resultsArray -startPosition 1 -searchString "TicketsProduced:"
    [int]$tickets2Location = findInArray -searchArray $resultsArray -startPosition ($tickets1Location+1) -searchString "TicketsProduced:"
    [int]$tickets3Location = findInArray -searchArray $resultsArray -startPosition ($tickets2Location+1) -searchString "TicketsProduced:"
    [int]$tickets4Location = findInArray -searchArray $resultsArray -startPosition ($tickets3Location+1) -searchString "TicketsProduced:"
    [int]$tickets5Location = findInArray -searchArray $resultsArray -startPosition ($tickets4Location+1) -searchString "TicketsProduced:"
    if ($tickets5Location -eq $resultsArray.Count){
        $resultsArray += "ERROR"
    }

    loadArrayList $resultsArray[$name1Location-1] $resultsArray[$tickets1Location+1]
    loadArrayList $resultsArray[$name2Location-1] $resultsArray[$tickets2Location+1]
    loadArrayList $resultsArray[$name3Location-1] $resultsArray[$tickets3Location+1]
    loadArrayList $resultsArray[$name4Location-1] $resultsArray[$tickets4Location+1]
    loadArrayList $resultsArray[$name5Location-1] $resultsArray[$tickets5Location+1]
   
   <#
    Write-Debug $resultsArray[$name1Location-1]
    Write-Debug $resultsArray[$name2Location-1]
    Write-Debug $resultsArray[$name3Location-1]
    Write-Debug $resultsArray[$name4Location-1]
    Write-Debug $resultsArray[$name5Location-1]
    #>
}


<# ********************************************************************************************************************** #>


<<# ****************************************************  MAIN CODE  **************************************************** #>
$newDir = Get-Date -Format yyyy-MM-dd
$newDir = $imageDirIn + $newDir

#create dir for day's files
md -force $newDir
#move files from the root to the daily folder
moveFiles $imageDirIn $newDir
$cropDir = $newDir +"\tmp"
md -force $cropDir

createCroppedImages $cropDir

#loop through the cropped image files and call function getTicketData which does the call to Cognitive Services for OCR.
$srch = $CropDir + "\*.png"
$files = Get-ChildItem $srch

for ($i=0; $i -lt $files.Count; $i++) {
    Write-Debug "$(Get-CurrentLineNumber) File: $($files[$i].FullName)"
    getTicketData $files[$i].FullName
    
}


#now we have all of the Players and Coins in an arraylist.  Save the array list in a file
$dataFile = $newDir +"\data.csv"
#$collectionWithItems | Export-Csv $dataFile -notype

foreach ($item in $dataList) {
   $line = $item.playerID + "," + $item.ltTickets + "," + $(Get-Date -Format yyyy-MM-dd)
   Add-Content $dataFile $line
 }

 <# **************************************************  END MAIN CODE  ************************************************** #>