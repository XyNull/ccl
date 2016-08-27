# ccl
Java Version By XyNull.
Utility for counting code line's count.

## Usage

    ccl <RootFolder> <Suffix> [<Suffix>,...] [<Options>]

## Params
RootFolder: Folder to count.

Suffix: Suffix of code file. Start with '.'.

Options:
* --ignore-blank, -I: Ignore blank files & paths.
* PS.PS.Ignore blank lines is default.
* --verbose, -V: Show process log.

## Example

	ccl ProjectRoot .cpp .c .h -V -I

This will count all **.cpp/.c/.h file**s' **non-blank line**s' count in folder **ProjectRoot**, and **log will be print** during the procedures.

## Binaries
[Windows](http://www.vicey.cn/files/ccl.exe)
[Windows](http://github.com/XyNull/ccl)
