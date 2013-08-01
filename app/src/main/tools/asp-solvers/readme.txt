This folder shall contain the ASP Solvers that are not 
distributed within Angerona. The developer who contributes 
to the Angerona framework has to decide if he/she can accept 
the licenses used by the ASP solvers.

The following list gives the URLs of the Websites for
each ASP solver supported by Angerona.

dlv 		http://www.dlvsystem.com/dlv/
dlv-complex	https://www.mat.unical.it/dlv-complex
Clingo		http://potassco.sourceforge.net/

And this list maps a binary filename to the ASP solver
that is assumed by the configuration_template in the
'<angerona-dir>/software/app/src/main/config/' folder

Solver		Windows			Unix		Mac OS
........................................................
dlv 		dlv.exe			dlv			dlv.bin
dlv-complex	dlv-complex.exe	dlv-complex	dlv-complex.bin
Clingo		clingo.exe		clingo		clingo.bin