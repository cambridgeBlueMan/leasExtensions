Gaslight {
	var myServer, myOptions, myFunction;
	  // this is a normal constructor method
	    *new { |s|
		        ^super.new.init(s)
	    }
	    init { |s|
		myServer = s;  // ?? Server.default;
		myFunction = { |server|
			var octave = 0, notePlayers;
var netaddr = NetAddr("10.0.1.2", 9000);
			// ******************************
			// confirm connection to phone
			OSCFunc.new({|msg|
				// "got it!".postln;
				if (msg[1] == 1, {
					netaddr.sendMsg("/1/pinger/color", "green")
				},{
					netaddr.sendMsg("/1/pinger/color", "red")
				}) // end if
			},
			"/1/pinger"
			);
			// *********************
			// get ping from phone
			OSCFunc.new({|args|
				"got pinged!!!".postln;
			},
			"/ping");

			// **********************
			// keyboard responders
			notePlayers = List.new;
			15.do ({|i|
				// i.postln;
				notePlayers.add(
					OSCFunc.new({|args|
						if (args[1] > 0,
							{
								//m.noteOn(1, (58 + i + (octave*12)).postln, 60);
								Synth.new(\harps2, [\amp, args[1], \freq, (58 + i + (octave*12)).midicps ]);
							},
							{
								//m.noteOff(1, (58 + i + (octave*12)).postln, 60);
							}
						);
						// should also free synth on  note off
					}, // osc func
					"/1/push" ++ (i).asString
					);
				);
			}); // end 12.do

			// ***************
			// shift octave up
			// ***************

			OSCFunc({|args|
				if (args[1] == 1,
					{
						octave = octave + 1;
						if (octave > 2, {octave = 2});
						netaddr.sendMsg("/octave", octave);
				}); //end outer if
			},
			"/plus"
			); // end osc func

			// *****************
			// shift octave down
			// *****************

			OSCFunc({|args|
				if (args[1] == 1,
					{
						octave = octave - 1;
						if (octave <  -2, {octave = -2});
						netaddr.sendMsg("/octave", octave);
				}); //end outer if
			},
			"/minus"
			); // end osc func
			// 	"------------The server '%' has booted.------------\n".postf(server);
		}; // end of colliderphone functions
		ServerBoot.add(myFunction, myServer);
		myOptions = myServer.options;  //Options.new;
		myOptions.memSize = 32768; // 2.pow(30);
		myOptions.sampleRate = 48000;
		myOptions.device = "Scarlett 2i2 USB";
		myServer.options = myOptions;
		myServer.boot;
	} // end init
} // end class