//
//  BonjourServicesDiscoverer.m
//  BonjouriOS
//
//  Created by Spirit on 7/23/14.
//  Copyright (c) 2014 funymph. All rights reserved.
//

#import "BonjourServicesDiscoverer.h"

@implementation BonjourServicesDiscoverer {
	NSNetServiceBrowser* _browser;
}

- (instancetype)initWithContainer:(id<BonjourDiscoveredServices>)container {
	if (self = [super init]) {
		_serviceContainer = container;
		_browser = [[NSNetServiceBrowser alloc] init];
		_browser.delegate = self;
		_browser.includesPeerToPeer = YES;
	}
	return self;
}

- (void)start {
	[_browser searchForServicesOfType:@"_chat._tcp." inDomain:@"local."];
}

- (void)stop {
	[_browser stop];
}

- (void)netServiceBrowser:(NSNetServiceBrowser*)browser didFindService:(NSNetService*)service moreComing:(BOOL)moreComing {
	NSLog(@"service: %@ found", service);
	dispatch_async(dispatch_get_main_queue(), ^() {
		[self.serviceContainer addService:service];
	});
}

- (void)netServiceBrowser:(NSNetServiceBrowser*)aNetServiceBrowser didNotSearch:(NSDictionary*)errorDict {
	NSLog(@"failed to discover services, due to %@", errorDict);
}

- (void)netServiceBrowser:(NSNetServiceBrowser*)browser didRemoveService:(NSNetService*)service moreComing:(BOOL)moreComing {
	NSLog(@"service: %@ removed", service);
	dispatch_async(dispatch_get_main_queue(), ^() {
		[self.serviceContainer removeService:service];
	});
}

- (void)netServiceBrowserDidStopSearch:(NSNetServiceBrowser*)aNetServiceBrowser {
	NSLog(@"service browser stopped");
}

@end
