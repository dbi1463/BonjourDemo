//
//  BonjourDiscoveredServicesViewController.m
//  BonjouriOS
//
//  Created by Spirit on 7/23/14.
//  Copyright (c) 2014 funymph. All rights reserved.
//

#import "BonjourDiscoveredServicesViewController.h"

#import "BonjourServicesDiscoverer.h"
#import "BonjourDiscoveredServicesDataSource.h"

@implementation BonjourDiscoveredServicesViewController {
	NSNetService* _publishingService;
	BonjourServicesDiscoverer* _servicesDiscoverer;
	BonjourDiscoveredServicesDataSource* _services;
}

- (void)viewDidLoad {
	[super viewDidLoad];
	self.title = @"Bonjour";
	_services = [[BonjourDiscoveredServicesDataSource alloc] init];
	_services.delegate = self;
	self.servicesView.dataSource = _services;
	_publishingService = [[NSNetService alloc] initWithDomain:@"" type:@"_chat._tcp." name:@"someone on iOS" port:9166];
	_publishingService.delegate = self;
	_servicesDiscoverer = [[BonjourServicesDiscoverer alloc] initWithContainer:_services];
}

- (void)discoveredServicesChanged {
	[self.servicesView reloadData];
}

- (IBAction)pushlishService:(id)sender {
	[self.publishButton setEnabled:NO];
	if (self.published) {
		[_publishingService stop];
	}
	else {
		[self.publishButton setTitle:@"Publishing" forState:UIControlStateNormal];
		[_publishingService publish];
	}
}

- (IBAction)discoverServices:(id)sender {
	if (self.discovering) {
		[_servicesDiscoverer stop];
		self.discovering = NO;
		[self.discoverButton setTitle:@"Discover" forState:UIControlStateNormal];
	}
	else {
		[_servicesDiscoverer start];
		self.discovering = YES;
		[self.discoverButton setTitle:@"Discovering" forState:UIControlStateNormal];
	}
}

- (void)netServiceDidStop:(NSNetService*)sender {
	[self resetPublishStatus];
	NSLog(@"service depublished");
}

- (void)netService:(NSNetService*)sender didNotPublish:(NSDictionary*)errorDict {
	[self resetPublishStatus];
	NSLog(@"failed to publish services: %@, due to %@", sender.description, errorDict);
}

- (void)netServiceDidPublish:(NSNetService *)sender {
	self.published = YES;
	[self.publishButton setEnabled:YES];
	[self.publishButton setTitle:@"Depublish" forState:UIControlStateNormal];
	NSLog(@"service: %@ published", sender.description);
}

- (void)resetPublishStatus {
	self.published = NO;
	[self.publishButton setEnabled:YES];
	[self.publishButton setTitle:@"Publish" forState:UIControlStateNormal];
}

@end
